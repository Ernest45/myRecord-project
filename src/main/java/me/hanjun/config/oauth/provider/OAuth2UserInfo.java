package me.hanjun.config.oauth.provider;

import java.util.Map;

public interface OAuth2UserInfo {

    OAuth2Provider provider();

    String getAccessToken();

    Map<String, Object> getAttributes();

    String getId();

    String getEmail();

    String getName();

    String getNickname();
}
