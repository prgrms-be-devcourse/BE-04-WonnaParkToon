package com.wonnapark.wnpserver.domain.episode.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EpisodeThumbnailUpdateRequest(
        @NotBlank @Length(max = 255)
        String thumbnail
) {
}
