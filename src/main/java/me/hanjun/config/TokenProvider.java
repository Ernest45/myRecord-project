package me.hanjun.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.hanjun.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);

    }


    // note 1. 토큰 생성 메서드
    private String makeToken(Date expiry, User user) {


        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 type : jwt
                .setIssuer(jwtProperties.getIssuer())
                // 내용 iss : ***@!!!.com(properties 파일에서 설정 값)
                .setIssuedAt(now) //내용 iat : 현재시간
                .setExpiration(expiry) //내용 exp : expire 변수 값
                .setSubject(user.getEmail()) // 내용 sub : 유저 이메일
                .claim("id", user.getId()) // 클레임 id : 유저 id
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                // 서명 비밀 값과 함께 해시 값을 hs256방식으로 암호화
                .compact();
    }

    // note 2. jwt 토큰 유효성 검증 메서드
    public boolean validToken(String token) {

        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀 값으로 복호화
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) { //복호화 과정에서 에러가 나면 유효하지 않은 토큰!
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
    }
}
