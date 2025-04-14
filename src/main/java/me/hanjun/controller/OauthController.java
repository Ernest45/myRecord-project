//package me.hanjun.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class OauthController {
//    private final OauthService oauthService;
//
//    @GetMapping("/login/oauth2/code/{provider}")
//    public ResponseEntity<?> oauthLogin(
//            @PathVariable("provider") String provider,
//            @RequestParam("code") String code) {
//
//        try {
//            OauthResponse response = oauthService.handleOauthLogin(provider, code);
//            return ResponseEntity.ok(response);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//}
