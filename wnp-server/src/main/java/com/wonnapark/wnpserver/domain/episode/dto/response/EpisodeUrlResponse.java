package com.wonnapark.wnpserver.domain.episode.dto.response;

import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;

public record EpisodeUrlResponse(
        String url
) {

    public static EpisodeUrlResponse from(EpisodeUrl episodeUrl) {
        return new EpisodeUrlResponse(episodeUrl.getUrl());
    }

}
