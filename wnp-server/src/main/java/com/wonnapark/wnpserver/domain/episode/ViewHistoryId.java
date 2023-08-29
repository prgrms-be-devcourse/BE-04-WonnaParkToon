package com.wonnapark.wnpserver.domain.episode;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewHistoryId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "episode_id", nullable = false)
    private Long episodeId;

    @Builder
    private ViewHistoryId(Long userId, Long episodeId) {
        this.userId = userId;
        this.episodeId = episodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewHistoryId that = (ViewHistoryId) o;
        return Objects.equals(getEpisodeId(), that.getEpisodeId()) && Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEpisodeId(), getUserId());
    }

}
