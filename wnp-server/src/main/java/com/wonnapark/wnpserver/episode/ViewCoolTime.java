package com.wonnapark.wnpserver.episode;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash(value = "episode_view_infos", timeToLive = 12 * 12 * 60)
public class ViewCoolTime {

    @Id
    private String key;
    private LocalDateTime viewTime;

    public ViewCoolTime(String key, LocalDateTime viewTime) {
        this.key = key;
        this.viewTime = viewTime;
    }

}
