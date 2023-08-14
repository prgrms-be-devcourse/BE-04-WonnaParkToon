package com.wonnapark.wnpserver.domain.episode.dto.request;

import com.wonnapark.wnpserver.domain.episode.Episode;

import java.time.LocalDateTime;

public record EpisodeCreationRequest(
        String title,
        LocalDateTime releaseDateTime,
        String thumbnail,
        String artistComment
) {

    public static Episode toEntity(EpisodeCreationRequest request) {
        return Episode.builder()
                .title(request.title)
                .releaseDateTime(request.releaseDateTime)
                .thumbnail(request.thumbnail)
                .artistComment(request.artistComment)
                .build();
    }

}
