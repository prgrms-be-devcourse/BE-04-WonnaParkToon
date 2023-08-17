package com.wonnapark.wnpserver.domain.episode.dto.request;

import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
import jakarta.validation.constraints.NotBlank;

public record EpisodeUrlCreationRequest(
        @NotBlank
        String episodeUrl,
        int order
) {

    public static EpisodeUrl toEntity(EpisodeUrlCreationRequest request) {
        return EpisodeUrl.builder()
                .url(request.episodeUrl)
                .order(request.order)
                .build();
    }

}
