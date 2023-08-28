package com.wonnapark.wnpserver.domain.episode.dto.request;

import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;

import java.util.List;

public record EpisodeUrlsUpdateRequest(
        List<String> urls
) {

    public List<EpisodeUrl> toEntityList() {
        return urls.stream()
                .map(url -> EpisodeUrl.builder()
                        .url(url)
                        .build()
                )
                .toList();
    }

}
