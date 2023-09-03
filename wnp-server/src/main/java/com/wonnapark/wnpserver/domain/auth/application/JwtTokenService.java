package com.wonnapark.wnpserver.domain.auth.application;

import com.wonnapark.wnpserver.domain.auth.RefreshToken;
import com.wonnapark.wnpserver.domain.auth.config.JwtProperties;
import com.wonnapark.wnpserver.domain.auth.config.TokenConstants;
import com.wonnapark.wnpserver.domain.auth.dto.AccessTokenResponse;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.domain.auth.dto.RefreshTokenResponse;
import com.wonnapark.wnpserver.domain.auth.infrastructure.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenService {


    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Key key;

    public JwtTokenService(JwtProperties jwtProperties, RefreshTokenRepository refreshTokenRepository) {
        this.jwtProperties = jwtProperties;
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public AccessTokenResponse generateAccessToken(AuthTokenRequest request) {
        long now = (new Date()).getTime();
        Date expiredAt = new Date(now + jwtProperties.accessTokenExpireTime());
        String accessToken = Jwts.builder()
                .claim(TokenConstants.AGE_CLAIM_NAME, request.age())
                .claim(TokenConstants.ROLE_CLAIM_NAME, request.role())
                .setSubject(String.valueOf(request.userId()))
                .setIssuer(TokenConstants.ISSUER)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return new AccessTokenResponse(TokenConstants.BEARER_TYPE, accessToken, jwtProperties.accessTokenExpireTime() / TokenConstants.MILLI_SECOND);
    }

    public RefreshTokenResponse generateRefreshToken(AuthTokenRequest request) {
        long now = (new Date()).getTime();
        Date expiredAt = new Date(now + jwtProperties.refreshTokenExpireTime());
        String refreshToken = Jwts.builder()
                .setIssuer(TokenConstants.ISSUER)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .userId(request.userId())
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return new RefreshTokenResponse(TokenConstants.BEARER_TYPE, refreshToken);
    }

    public AuthTokenResponse generateAuthToken(AuthTokenRequest request) {
        AccessTokenResponse accessTokenResponse = generateAccessToken(request);
        RefreshTokenResponse refreshTokenResponse = generateRefreshToken(request);
        return AuthTokenResponse.builder()
                .grantType(TokenConstants.BEARER_TYPE)
                .accessToken(accessTokenResponse.accessToken())
                .accessTokenExpiresIn(jwtProperties.accessTokenExpireTime() / TokenConstants.MILLI_SECOND)
                .refreshToken(refreshTokenResponse.refreshToken())
                .build();
    }

}
