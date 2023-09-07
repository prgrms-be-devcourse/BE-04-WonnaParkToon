package com.wonnapark.wnpserver.domain.oauth;

public enum OAuthProvider {
    KAKAO,
    NAVER;

    public static OAuthProvider from(String platform) {
        return OAuthProvider.valueOf(platform.toUpperCase());
    }
}
