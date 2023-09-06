package com.wonnapark.wnpserver.domain.auth.dto;

public record AccessTokenResponse(
        String grantType,
        String accessToken,
        Long accessTokenExpiresIn
) {
}
