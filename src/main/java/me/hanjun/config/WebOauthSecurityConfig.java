package me.hanjun.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanjun.config.oauth.*;
import me.hanjun.repository.RefreshTokenRepository;
import me.hanjun.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebOauthSecurityConfig {

    private final Oauth2UserCustomService oauth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;


    @Bean
    public WebSecurityCustomizer configure() {
        //스프링 시큐리티 기능 비활성화
        return (web -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img**", "/css/**", "js/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        //토큰 방식으로 인증을 하기 때문에 기존에 사용하던 폼로그인, 세션 비활성화
        http.csrf().disable()
                //CSRF(사이트 간 요청 위조) 보호 비활성화. 토큰 기반 인증(JWT)에서는 세션이 없으므로 CSRF 공격 위험이 낮아서 꺼둠.
                .httpBasic().disable() //HTTP Basic 인증(브라우저 팝업 로그인) 비활성화
                .formLogin().disable() //폼로그인 비활성화
                .logout().disable(); // 아래에서 커스터마이징.


        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //SessionCreationPolicy.STATELESS: 세션을 사용하지 않음.
        //왜?: JWT 토큰으로 인증하니까 서버가 상태(세션)를 유지할 필요 없음. 클라이언트가 토큰을 헤더에 실어 보냄.

        //헤더를 확인할 커스텀 필드 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //addFilterBefore(...UsernamePasswordAuthenticationFilter.class):
        // Spring Security의 기본 인증 필터(UsernamePasswordAuthenticationFilter) 전에 실행.


        // 토큰 재발급 URL는 인증 없이도 접근 가능하도록 설정, 나머지 API URL은 인증 필요
        http.authorizeHttpRequests()
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll();

        http.oauth2Login()
                .loginPage("/login")
                .authorizationEndpoint()
                // Authorization 요청과 관련된 상태 저장
                .authorizationRequestRepository(oAuth2AuthorizationREquestBasedOnCookieRepository())
                .and()
                .successHandler(oAuth2SuccessHandler()) // 인증 성공 시 실행할 핸들러
                .failureHandler((request, response, exception) -> {
                    log.error("OAuth2 authentication failed: {}", exception.getMessage(), exception);
                    System.out.println("왜안돼");
                    response.sendRedirect("/login?error=" + URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8));
                })
                .userInfoEndpoint()
                .userService(oauth2UserCustomService);

        http.logout()
                .logoutSuccessUrl("/login");

//         api로 시작하는 uri인 경우 401 상태 코드를 반환하도록 예외처리
//        http.exceptionHandling()
//                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
//                        , new AntPathRequestMatcher("/api/**"));


        return http.build();

    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository, oAuth2AuthorizationREquestBasedOnCookieRepository(),
                userService);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    // 세션을 안쓰고 쿠키로 활용하기 위해
    public OAuth2AuthorizationRequestBasedOnCookieRepository
    oAuth2AuthorizationREquestBasedOnCookieRepository() {

        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authBuilder.build();
    }
}
