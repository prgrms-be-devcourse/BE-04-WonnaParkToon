package com.wonnapark.wnpserver.domain.auth;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 604800)
@Getter
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String refreshToken;

    private Long userId;
}
