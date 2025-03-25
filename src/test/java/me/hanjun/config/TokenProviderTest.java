package me.hanjun.config;

import io.jsonwebtoken.Jwts;
import me.hanjun.domain.User;
import me.hanjun.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

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

    @Test
    @DisplayName("generateToken 검증")
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
    void validToken() {
    }

    @Test
    void getAuthentication() {
    }

    @Test
    void getUserId() {
    }
}