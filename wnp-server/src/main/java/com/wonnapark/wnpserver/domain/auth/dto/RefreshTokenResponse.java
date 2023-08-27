package com.wonnapark.wnpserver.domain.auth.dto;

public record RefreshTokenResponse(
        String grantType,
        String refreshToken
) {
}
