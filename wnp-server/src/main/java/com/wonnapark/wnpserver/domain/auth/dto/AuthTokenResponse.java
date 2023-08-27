package com.wonnapark.wnpserver.domain.auth.dto;

import lombok.Builder;

@Builder
public record AuthTokenResponse(
        String grantType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
) {
}
