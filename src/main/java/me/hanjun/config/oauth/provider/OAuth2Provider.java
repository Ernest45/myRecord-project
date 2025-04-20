package me.hanjun.config.oauth.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
    NAVER("naver"),
    KAKAO("kakao"),
    GOOGLE("google");

    private final String registrationId;
}
