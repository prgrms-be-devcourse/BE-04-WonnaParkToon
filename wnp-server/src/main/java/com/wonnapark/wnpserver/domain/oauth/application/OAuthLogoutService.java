package com.wonnapark.wnpserver.domain.oauth.application;

import com.wonnapark.wnpserver.domain.auth.application.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLogoutService {

    private final JwtTokenService jwtTokenService;

    public void logout(Long userId, String accessToken) {
        jwtTokenService.logoutAccessToken(accessToken);
        jwtTokenService.deleteRefreshTokenByUserId(userId);
    }

}
