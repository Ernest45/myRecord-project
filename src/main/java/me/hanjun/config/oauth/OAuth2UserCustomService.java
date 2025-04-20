package me.hanjun.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanjun.config.oauth.provider.OAuth2UserInfo;
import me.hanjun.config.oauth.provider.OAuth2UserInfoFactory;
import me.hanjun.domain.User;
import me.hanjun.repository.UserRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    // 이 메서드로 자동으로 http요청을 보내주고 그걸 난 저장만하면 됨 (파싱  url마추고 다 해줌..)
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        log.info("1Starting loadUser for OAuth2UserRequest: {}", userRequest);
        OAuth2User user = super.loadUser(userRequest);
        if (user == null) {
            throw new OAuth2AuthenticationException("OAuth2 사용자 정보를 가져올 수 없음");
        }
        try{
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //OAuth2UserRequest 네이버 구현 시 추가
        if ("kakao".equals(registrationId)) {
            return processKakaoUser(userRequest,user);
        }

        saveOrUpdate(user);

        return user;

    }catch (Exception ex) {
            log.error("OAuth2 사용자 처리 중 오류 발생", ex);
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }


    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        if (email == null) {

            throw new OAuth2AuthenticationException("Email not found in OAuth2 user attributes");
        }
        String name = (String) attributes.get("name");

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .nickname(name)
                        .build());

        return userRepository.save(user);

    }

    private OAuth2User processKakaoUser(OAuth2UserRequest userRequest,OAuth2User oAuth2User) {
        String accessToken = userRequest.getAccessToken().getTokenValue();
        log.debug("Kakao OAuth2User attributes: {}", oAuth2User.getAttributes());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.create(
                "kakao",
                accessToken,
                oAuth2User.getAttributes()
        );

        String email = userInfo.getEmail();
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from Kakao");
        }

        String nickname = userInfo.getNickname();

        // DB에 사용자 정보 저장 또는 업데이트
        User user = userRepository.findByEmail(email)
                .map(existingUser -> existingUser.update(nickname))
                .orElse(User.builder()
                        .email(email)
                        .nickname(nickname)
                        .build());

        user = userRepository.save(user);

        Map<String, Object> attributes = new HashMap<>();
        // 원본 속성을 안전하게 복사
        for (Map.Entry<String, Object> entry : oAuth2User.getAttributes().entrySet()) {
            attributes.put(entry.getKey(), entry.getValue());
        }

//         추가 정보 설정
        attributes.put("email", email);
        attributes.put("nickname", nickname);

        // 권한 정보 설정
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("user")
        );

        // DefaultOAuth2User 객체 생성하여 반환
        return new DefaultOAuth2User(
                authorities,
                attributes,
                "id" // nameAttributeKey - Kakao의 경우 "id"가 사용자 식별자
        );


    }
}
