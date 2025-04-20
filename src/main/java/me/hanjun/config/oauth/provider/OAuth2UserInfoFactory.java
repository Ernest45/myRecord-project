package me.hanjun.config.oauth.provider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo create(String registrationId,
                                        String accessToken,
                                        Map<String, Object> attributes) {

//        if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
            return new OAuth2KakaoUserInfo(accessToken, attributes);

    }
}
