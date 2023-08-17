package com.wonnapark.wnpserver.domain.auth.token;

public record AuthToken(
        String accessToken,
        String refreshToken,
        String grantType,
        Long expiredAt
){
    public static AuthToken of(String accessToken, String refreshToken, String grantType, Long expiredAt) {
        return new AuthToken(accessToken, refreshToken, grantType, expiredAt);
    }
}
