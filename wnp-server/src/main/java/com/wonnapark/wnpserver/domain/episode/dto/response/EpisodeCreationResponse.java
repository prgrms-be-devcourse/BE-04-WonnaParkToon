package com.wonnapark.wnpserver.domain.episode.dto.response;

import com.wonnapark.wnpserver.domain.episode.Episode;
import lombok.Builder;

@Builder
public record EpisodeCreationResponse(
        Long id
) {
    public static EpisodeCreationResponse from(Episode episode) {
        return EpisodeCreationResponse.builder()
                .id(episode.getId())
                .build();
    }
}
