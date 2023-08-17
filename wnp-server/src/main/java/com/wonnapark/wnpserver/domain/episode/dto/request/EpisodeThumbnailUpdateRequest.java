package com.wonnapark.wnpserver.domain.episode.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EpisodeThumbnailUpdateRequest(
        @NotBlank
        String thumbnail
) {
}
