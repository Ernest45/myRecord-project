//package me.hanjun.config.oauth;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
//import org.springframework.stereotype.Component;
//@Slf4j
//@Component
//public class CustomOAuth2LoginAuthenticationFilter extends OAuth2LoginAuthenticationFilter {
//
//    public CustomOAuth2LoginAuthenticationFilter(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientService authorizedClientService,
//            AuthenticationManager authenticationManager) {
//        super(clientRegistrationRepository, authorizedClientService);
//        this.setAuthenticationManager(authenticationManager);
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException {
//        log.info("Attempting OAuth2 authentication for request: {}", request.getRequestURI());
//        try {
//            Authentication authentication = super.attemptAuthentication(request, response);
//            log.info("Authentication successful in attemptAuthentication: {}", authentication);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.info("Authentication set in SecurityContextHolder: {}", SecurityContextHolder.getContext().getAuthentication());
//            return authentication;
//        } catch (Exception e) {
//            log.error("OAuth2 authentication failed in attemptAuthentication: {}", e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                            Authentication authentication) throws java.io.IOException {
//        log.info("Successful authentication, calling successHandler for: {}", authentication);
//        super.successfulAuthentication(request, response, authentication);
//        log.info("SuccessHandler completed");
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                              AuthenticationException failed) throws java.io.IOException {
//        log.error("Unsuccessful authentication, calling failureHandler: {}", failed.getMessage(), failed);
//        super.unsuccessfulAuthentication(request, response, failed);
//        log.info("FailureHandler completed");
//    }
//}
