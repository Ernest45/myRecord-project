package me.hanjun.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanjun.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
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
                    .setSigningKey(jwtProperties.getSecretKey()) //
                    .parseClaimsJws(token);  // note 3,4 형식이 올바르지 않거나, 지원하지 않는 경우
            log.info("jwtProperties.getSecretKey() :", jwtProperties.getSecretKey());

            return true;
        } catch (Exception e) { //복호화 과정에서 에러가 나면 유효하지 않은 토큰!
            System.out.println(e);
            return false;
        }
    }


    // note 3 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authority = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER"));


        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authority), token, authority);
    }

    // note 4 토큰 기반으로 유저 id 를 가져오는 메서드

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        // 프로퍼티스 파일에 저장한 비밀 값으로 토큰을 복호화 한 뒤 클레임을 가져오는 메서드

        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();




    }


}
