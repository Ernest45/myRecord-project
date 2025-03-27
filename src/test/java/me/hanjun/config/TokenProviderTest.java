package me.hanjun.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import me.hanjun.domain.User;
import me.hanjun.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;

//    @Test
//    @DisplayName("JwtFactory 디버깅: getter가 제대로 동작하는지 확인")
//    void debugJwtFactory() {
//        // 디버깅 코드 추가
//        JwtFactory factory = new JwtFactory("test", new Date(), new Date(), emptyMap());
//        System.out.println(factory.getSubject()); // "test" 출력 확인
//    }

    @Test
    @DisplayName("generateToken (): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다")
    void generateToken() {
        //given
        User testUSer = userRepository.save(User.builder()
                .email("test@email.com")
                .password("test")
                .build());

        //When
        String token = tokenProvider.generateToken(testUSer, Duration.ofDays(14));

        //then
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUSer.getId());

    }

    @Test
    @DisplayName("validToken_invalidToken() : 만료된 토큰인 때에 유효성 검증에 실패한다.")
    void validToken_invalidToken() {
        //given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        //when

        boolean result = tokenProvider.validToken(token);

        assertThat(result).isFalse();

    }

    @Test
    @DisplayName("validToken() : 유효한 토큰일 때에 유효성 검증에 성공한다.")
    void validToken() {

        //given
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        //when

        boolean result = tokenProvider.validToken(token);

        assertThat(result).isTrue();



    }

    @Test
    @DisplayName("getAuthentication() : 토큰 기반으로 인증 정보를 가져올 수 있다.")
    void getAuthentication() {


        //given
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);
        //when
        Authentication authentication = tokenProvider.getAuthentication(token);

        //then

        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);

        // note UserDeatails의 타입으로 형 변환 후 가져오기!


    }

    @Test
    @DisplayName("getUserId() : 토큰으로 유저 id를 가져올 수 있다.")
    void getUserId() {

        //given
        Long id = 1L;
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        String token = JwtFactory.builder()
                .claims(map)
                .build()
                .createToken(jwtProperties);

        //when
//        Claims claims = Jwts.parser()
//                .setSigningKey(jwtProperties.getSecretKey())
//                .parseClaimsJws(token)
//                .getBody();
//        claims.get("id", Long.class);
        Long idByToken = tokenProvider.getUserId(token);

        assertThat(id).isEqualTo(idByToken);



    }
}