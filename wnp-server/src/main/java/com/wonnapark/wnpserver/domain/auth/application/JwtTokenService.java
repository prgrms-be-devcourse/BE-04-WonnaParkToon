package com.wonnapark.wnpserver.domain.auth.application;

import com.wonnapark.wnpserver.domain.auth.RefreshToken;
import com.wonnapark.wnpserver.domain.auth.dto.AccessTokenResponse;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.domain.auth.dto.RefreshTokenResponse;
import com.wonnapark.wnpserver.domain.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.domain.auth.infrastructure.RefreshTokenRepository;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.global.response.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

//@Component
public class JwtTokenService {

    private static final String BEARER_TYPE = "Bearer";
    private static final String ISSUER = "wonnapark";
    private static final String DELIMITER = "&";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    private static final long MILLI_SECOND = 1000L;

    private final RefreshTokenRepository refreshTokenRepository;
    private final Key key;

    public JwtTokenService(@Value("${jwt.secret-key}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public AccessTokenResponse generateAccessToken(AuthTokenRequest request) {
        long now = (new Date()).getTime();
        Date expiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String subject = request.userId() + DELIMITER + request.age();
        String accessToken = Jwts.builder()
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return new AccessTokenResponse(BEARER_TYPE, accessToken, ACCESS_TOKEN_EXPIRE_TIME / MILLI_SECOND);
    }

    public RefreshTokenResponse generateRefreshToken(AuthTokenRequest request) {
        long now = (new Date()).getTime();
        Date expiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .userId(request.userId())
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return new RefreshTokenResponse(BEARER_TYPE, refreshToken);
    }

    public AuthTokenResponse generateAuthToken(AuthTokenRequest request) {
        AccessTokenResponse accessTokenResponse = generateAccessToken(request);
        RefreshTokenResponse refreshTokenResponse = generateRefreshToken(request);
        return AuthTokenResponse.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessTokenResponse.accessToken())
                .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRE_TIME / MILLI_SECOND)
                .refreshToken(refreshTokenResponse.refreshToken())
                .build();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
//        } catch (ExpiredJwtException expiredJwtException) {
//            throw new JwtInvalidException(ErrorCode.EXPIRED_TOKEN.getMessage(), expiredJwtException.getCause());
        } catch (SignatureException signatureException) {
            throw new JwtInvalidException(ErrorCode.WRONG_SIGNATURE_TOKEN.getMessage(), signatureException.getCause());
        } catch (MalformedJwtException | UnsupportedJwtException unsupportedJwtException) {
            throw new JwtInvalidException(ErrorCode.UNSUPPORTED_TOKEN.getMessage(), unsupportedJwtException.getCause());
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new JwtInvalidException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), illegalArgumentException.getCause());
        }
    }

    public UserInfo extractUserInfo(String token) {
        Claims claims = parseClaims(token);
        String subject = claims.getSubject();
        String[] extracted = subject.split(DELIMITER);
        return UserInfo.from(extracted);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
