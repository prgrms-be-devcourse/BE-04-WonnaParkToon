package com.wonnapark.wnpserver.domain.episode.dto.request;

import com.wonnapark.wnpserver.domain.episode.Episode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record EpisodeCreationRequest(
        @NotBlank @Length(min = 1, max = 35)
        String title,
        @NotNull
        LocalDateTime releaseDateTime,
        @NotBlank
        String thumbnail,
        @NotBlank @Length(min = 1, max = 100)
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
