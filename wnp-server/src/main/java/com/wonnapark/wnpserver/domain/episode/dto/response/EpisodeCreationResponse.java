package com.wonnapark.wnpserver.domain.episode.dto.response;

import com.wonnapark.wnpserver.domain.episode.Episode;

public record EpisodeCreationResponse(
        Long id
) {

    public static EpisodeCreationResponse from(Episode episode) {
        return new EpisodeCreationResponse(episode.getId());
    }

}
