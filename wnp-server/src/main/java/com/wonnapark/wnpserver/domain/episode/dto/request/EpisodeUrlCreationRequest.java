package com.wonnapark.wnpserver.domain.episode.dto.request;

import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EpisodeUrlCreationRequest(
        @NotBlank @Length(max = 255)
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
