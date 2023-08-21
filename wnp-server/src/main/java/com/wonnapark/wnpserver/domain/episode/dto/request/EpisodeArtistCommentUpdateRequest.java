package com.wonnapark.wnpserver.domain.episode.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EpisodeArtistCommentUpdateRequest(
        @NotBlank @Length(min = 1, max = 100)
        String artistComment
) {
}
