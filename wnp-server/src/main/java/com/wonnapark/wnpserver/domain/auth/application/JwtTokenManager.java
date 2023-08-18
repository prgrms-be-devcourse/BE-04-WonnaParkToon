package com.wonnapark.wnpserver.domain.auth.application;

import com.wonnapark.wnpserver.global.exception.auth.JwtInvalidException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenManager {

    private static final String ISSUER = "wonnapark";
    private final Key key;

    public JwtTokenManager(@Value("${jwt.secret-key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractSubject(String token) {
        Claims claims = authenticate(token);
        System.out.println("claims = " + claims);
        return claims.getSubject();
    }

    public Claims authenticate(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new JwtInvalidException("signature key is different");
        } catch (ExpiredJwtException e) {
            throw new JwtInvalidException("expired token");
        } catch (MalformedJwtException e) {
            throw new JwtInvalidException("malformed token");
        } catch (IllegalArgumentException e) {
            throw new JwtInvalidException("using illegal argument like null");
        }
    }

}
