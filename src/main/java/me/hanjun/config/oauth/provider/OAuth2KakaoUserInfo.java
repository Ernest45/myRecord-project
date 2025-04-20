package me.hanjun.config.oauth.provider;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OAuth2KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String accessToken;
    private final String id;
    private final String email;
    private final String name;
    private final String nickname;

    public OAuth2KakaoUserInfo(String accessToken, Map<String, Object> attributes) {
        this.accessToken = accessToken;
        Map<String, Object> kakaoAccount =
                (Map<String, Object>) attributes.get("kakao_account");
      Map<String, Object> kakaoProfile =
                (Map<String, Object>) kakaoAccount.get("profile");
        this.attributes = attributes;

        this.id = attributes.get("id").toString();
        this.email = (String) kakaoAccount.get("email");
        this.name = null;
        this.nickname = (String) kakaoProfile.get("nickname");
//        this.attributes.put("id", id);
//        this.attributes.put("email", this.email);
//        this.attributes.put("nickname", this.nickname);
        // 쉽게 접근하기 위해서
    }

    @Override
    public OAuth2Provider provider() {
        return OAuth2Provider.KAKAO;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNickname() {
        return nickname;
    }
}
