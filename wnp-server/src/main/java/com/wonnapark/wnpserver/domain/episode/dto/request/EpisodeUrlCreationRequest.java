package com.wonnapark.wnpserver.domain.episode.dto.request;

import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EpisodeUrlCreationRequest(
        @NotBlank(message = "에피소드 URL은 공백이나 null일 수 없습니다.")
        @Length(max = 255, message = "에피소드 URL 길이는 255자 이하여야 합니다.")
        String episodeUrl,
        int order
) {

    public EpisodeUrl toEntity() {
        return EpisodeUrl.builder()
                .url(this.episodeUrl)
                .order(this.order)
                .build();
    }

}
