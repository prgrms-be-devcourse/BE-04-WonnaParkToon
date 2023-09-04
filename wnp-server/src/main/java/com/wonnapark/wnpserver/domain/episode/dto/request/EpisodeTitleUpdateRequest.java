package com.wonnapark.wnpserver.domain.episode.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EpisodeTitleUpdateRequest(
        @NotBlank @Length(min = 1, max = 35)
        String title
) {
}
