package com.wonnapark.wnpserver.domain.episode.dto.request;

import java.time.LocalDateTime;

public record EpisodeReleaseDateTimeUpdateRequest(
        LocalDateTime releaseDateTime
) {
}
