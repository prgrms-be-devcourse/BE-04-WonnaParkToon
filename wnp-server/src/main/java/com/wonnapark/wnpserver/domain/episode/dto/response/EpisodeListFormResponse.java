package com.wonnapark.wnpserver.domain.episode.dto.response;

import com.wonnapark.wnpserver.domain.episode.Episode;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EpisodeListFormResponse(
        Long id,
        String title,
        String thumbnail,
        LocalDate releaseDate
) {

    public static EpisodeListFormResponse from(Episode episode) {
        LocalDate releaseDate = episode.getReleaseDateTime().toLocalDate();

        return EpisodeListFormResponse.builder()
                .id(episode.getId())
                .title(episode.getTitle())
                .thumbnail(episode.getThumbnail())
                .releaseDate(releaseDate)
                .build();
    }

}
