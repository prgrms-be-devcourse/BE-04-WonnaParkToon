package com.wonnapark.wnpserver.domain.episode.dto.response;

import com.wonnapark.wnpserver.domain.episode.Episode;
import lombok.Builder;

import java.util.List;

@Builder
public record EpisodeDetailFormResponse(
        Long id,
        String artistComment,
        String title,
        List<EpisodeUrlResponse> episodeUrlResponses
) {

    public static EpisodeDetailFormResponse from(Episode episode) {
        List<EpisodeUrlResponse> episodeUrlResponses = episode.getEpisodeUrls().stream()
                .map(EpisodeUrlResponse::from)
                .toList();

        return EpisodeDetailFormResponse.builder()
                .id(episode.getId())
                .title(episode.getTitle())
                .artistComment(episode.getArtistComment())
                .episodeUrlResponses(episodeUrlResponses)
                .build();
    }

}
