package com.wonnapark.wnpserver.domain.auth.application;

import com.wonnapark.wnpserver.domain.auth.RefreshToken;
import com.wonnapark.wnpserver.domain.auth.SubjectInfo;
import com.wonnapark.wnpserver.domain.auth.dto.AuthToken;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.domain.auth.infrastructure.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokenService {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    private static final long MILLI_SECOND = 1000L;
    private static final String DELIMITER = "&";

    private final JwtTokenManager jwtTokenManager;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthToken generate(AuthTokenRequest request) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String subject = request.userId().toString() + DELIMITER + request.birthYear();
        String accessToken = jwtTokenManager.generate(subject, accessTokenExpiredAt);
        String refreshToken = jwtTokenManager.generate(subject, refreshTokenExpiredAt);

        RefreshToken refreshTokenForRedis = RefreshToken.builder()
                .refreshToken(refreshToken)
                .userId(request.userId())
                .build();

        refreshTokenRepository.save(refreshTokenForRedis);
        return AuthToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / MILLI_SECOND);
    }

    public SubjectInfo extractSubjectInfo(String accessToken) {
        String extracted = jwtTokenManager.extractSubject(accessToken);
        String[] subject = extracted.split(DELIMITER);
        return SubjectInfo.from(subject);
    }
}
