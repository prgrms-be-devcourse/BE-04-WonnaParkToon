package com.wonnapark.wnpserver.domain.episode.dto.response;

import com.wonnapark.wnpserver.domain.episode.Episode;

import java.util.List;

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

        return new EpisodeDetailFormResponse(
                episode.getId(),
                episode.getArtistComment(),
                episode.getTitle(),
                episodeUrlResponses
        );
    }

}
