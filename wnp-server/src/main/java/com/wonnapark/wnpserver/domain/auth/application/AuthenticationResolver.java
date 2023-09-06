package com.wonnapark.wnpserver.domain.auth.application;

import com.wonnapark.wnpserver.domain.auth.RefreshToken;
import com.wonnapark.wnpserver.domain.auth.config.JwtProperties;
import com.wonnapark.wnpserver.domain.auth.config.TokenConstants;
import com.wonnapark.wnpserver.domain.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.domain.auth.infrastructure.RefreshTokenRepository;
import com.wonnapark.wnpserver.domain.user.Role;
import com.wonnapark.wnpserver.global.auth.Authentication;
import com.wonnapark.wnpserver.global.response.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Strings;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class AuthenticationResolver {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final Key key;

    public AuthenticationResolver(RefreshTokenRepository refreshTokenRepository, RedisTemplate redisTemplate, JwtProperties jwtProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isValidAccessToken(String accessToken) {
        if (isValidToken(accessToken)) {
            String expiredAccessToken = redisTemplate.opsForValue().get(accessToken);

            if (!Strings.hasText(expiredAccessToken))
                return true;

            if (expiredAccessToken.equals(TokenConstants.LOGOUT))
                throw new JwtInvalidException(ErrorCode.EXPIRED_TOKEN);

            return true;
        }
        return false;
    }

    // 레디스로 만료기간을 같이 관리하니까 정합성이 안 맞을 수 있다.
    public boolean isValidRefreshToken(String token, Long userId) {
        if (isValidToken(token)) {
            RefreshToken refreshToken = refreshTokenRepository.findById(userId).orElseThrow(() -> new JwtInvalidException(ErrorCode.EXPIRED_TOKEN));
            return refreshToken.getValue().equals(token); // UUID 등을 필드로 줘서
        }
        return false;
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtInvalidException(ErrorCode.EXPIRED_TOKEN, expiredJwtException);
        } catch (SignatureException signatureException) {
            throw new JwtInvalidException(ErrorCode.WRONG_SIGNATURE_TOKEN, signatureException);
        } catch (MalformedJwtException | UnsupportedJwtException unsupportedJwtException) {
            throw new JwtInvalidException(ErrorCode.UNSUPPORTED_TOKEN, unsupportedJwtException);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new JwtInvalidException(ErrorCode.TOKEN_NOT_FOUND, illegalArgumentException);
        }
    }

    public Authentication extractAuthentication(String token) {
        Claims claims = parseClaims(token);
        String subject = claims.getSubject();
        int age = (int) claims.get(TokenConstants.AGE_CLAIM_NAME);
        String roleString = String.valueOf(claims.get(TokenConstants.ROLE_CLAIM_NAME));
        Role userRole = Role.valueOf(roleString);
        return Authentication.of(subject, age, userRole);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
