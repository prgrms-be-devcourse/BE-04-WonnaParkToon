package com.wonnapark.wnpserver.domain.auth;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 604800)
@Getter
public class RefreshToken {

    @Id
    private String refreshToken;

    private Long userId;

    @Builder
    private RefreshToken(String refreshToken, Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
