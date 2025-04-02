//package me.hanjun.config.oauth;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.access.ExceptionTranslationFilter;
//
//
//import org.springframework.security.web.access.ExceptionTranslationFilter;
//
//import java.io.IOException;
//
//@Slf4j
//public class CustomExceptionTranslationFilter extends ExceptionTranslationFilter {
//
//    public CustomExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint) {
//        super(authenticationEntryPoint);
//    }
//
//    @Override
//    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        try {
//            super.doFilter(request, response, chain);
//        } catch (Exception e) {
//            log.error("Spring Security exception at {}: {}", request.getRequestURI(), e.getMessage(), e);
//            throw e; // 예외를 다시 던져서 상위 필터에서 처리하도록 함
//        }
//    }
//}
