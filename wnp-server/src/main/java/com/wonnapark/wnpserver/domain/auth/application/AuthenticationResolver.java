package com.wonnapark.wnpserver.domain.auth.application;

import com.wonnapark.wnpserver.domain.auth.config.JwtProperties;
import com.wonnapark.wnpserver.domain.auth.config.TokenConstants;
import com.wonnapark.wnpserver.domain.auth.dto.RefreshTokenResponse;
import com.wonnapark.wnpserver.domain.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.domain.auth.infrastructure.RefreshTokenRepository;
import com.wonnapark.wnpserver.domain.user.Role;
import com.wonnapark.wnpserver.global.auth.Authentication;
import com.wonnapark.wnpserver.global.response.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class AuthenticationResolver {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Key key;

    public AuthenticationResolver(JwtProperties jwtProperties, RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public RefreshTokenResponse checkExpiredRefreshToken(Long userId) {
        return refreshTokenRepository.findById(userId)
                .map(RefreshTokenResponse::from)
                .orElseThrow(() -> new JwtInvalidException(ErrorCode.EXPIRED_TOKEN));
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
