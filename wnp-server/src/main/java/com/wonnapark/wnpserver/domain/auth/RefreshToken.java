package com.wonnapark.wnpserver.domain.auth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refreshToken", timeToLive = 604800)
@Getter
public class RefreshToken {

    @Id
    private Long userId;

    @Indexed
    private String tokenInfo;

    @Builder
    private RefreshToken(Long userId, String tokenInfo) {
        this.userId = userId;
        this.tokenInfo = tokenInfo;
    }
}
