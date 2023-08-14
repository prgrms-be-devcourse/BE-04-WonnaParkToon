package com.wonnapark.wnpserver.domain.episode.dto.response;

import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
import lombok.Builder;

@Builder
public record EpisodeUrlResponse(
        String url,
        int order
) {

    public static EpisodeUrlResponse from(EpisodeUrl episodeUrl) {
        return EpisodeUrlResponse.builder()
                .url(episodeUrl.getUrl())
                .order(episodeUrl.getOrder())
                .build();
    }

}
