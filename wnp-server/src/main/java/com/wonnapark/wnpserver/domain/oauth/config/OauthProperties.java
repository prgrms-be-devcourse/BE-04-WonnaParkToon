package com.wonnapark.wnpserver.domain.oauth.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OauthProperties {
    public static String KAKAO_REQUEST_TOKEN_URI = "/oauth/token";
    public static String KAKAO_REQUEST_INFO_URL = "/v2/user/me";

    private final Kakao kakao;

    @Getter
    @RequiredArgsConstructor
    public static class Kakao{
        private final String clientId;
        private final Url url;

        @Getter
        @RequiredArgsConstructor
        public final static class Url{
            private final String authUrl;
            private final String apiUrl;
            }
        }
}
