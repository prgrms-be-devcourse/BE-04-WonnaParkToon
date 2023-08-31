package com.wonnapark.wnpserver.domain.auth.dto;

import com.wonnapark.wnpserver.domain.auth.RefreshToken;
import com.wonnapark.wnpserver.domain.auth.TokenConstants;

public record RefreshTokenResponse(
        String grantType,
        String refreshToken
) {
    public static RefreshTokenResponse from(RefreshToken refreshToken) {
        return new RefreshTokenResponse(TokenConstants.BEARER_TYPE, refreshToken.getRefreshToken());
    }
}
