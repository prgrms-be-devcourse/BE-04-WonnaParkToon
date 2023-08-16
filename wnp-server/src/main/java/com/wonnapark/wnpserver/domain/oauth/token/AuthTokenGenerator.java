package com.wonnapark.wnpserver.domain.oauth.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokenGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    private static final long MILLI_SECOND = 1000L;

    private final JwtTokenGenerator jwtTokenGenerator;

    public AuthToken generate(Long userId) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String subject = userId.toString();
        String accessToken = jwtTokenGenerator.generate(subject, accessTokenExpiredAt);
        String refreshToken = jwtTokenGenerator.generate(subject, refreshTokenExpiredAt);

        return AuthToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / MILLI_SECOND);
    }

    public Long extractUserId(String accessToken) {
        return Long.valueOf(jwtTokenGenerator.extractSubject(accessToken));
    }
}
