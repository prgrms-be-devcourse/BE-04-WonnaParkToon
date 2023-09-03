package com.wonnapark.wnpserver.domain.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secretKey,
        long accessTokenExpireTime,
        long refreshTokenExpireTime
) {
}
