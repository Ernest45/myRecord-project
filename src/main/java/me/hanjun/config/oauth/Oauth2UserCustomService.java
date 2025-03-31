package me.hanjun.config.oauth;

import lombok.RequiredArgsConstructor;
import me.hanjun.domain.User;
import me.hanjun.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class Oauth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    // 이 메서드로 자동으로 http요청을 보내주고 그걸 난 저장만하면 됨 (파싱  url마추고 다 해줌..)
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;

    }

    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .nickname(name)
                        .build());

        return userRepository.save(user);

    }
}
