package com.wonnapark.wnpserver.domain.episode;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "view_historys")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewHistory {

    @EmbeddedId
    ViewHistoryId id;

    @Builder
    private ViewHistory(Long episodeId, Long userId) {
        id = ViewHistoryId.builder()
                .episodeId(episodeId)
                .userId(userId)
                .build();
    }

}
