package com.wonnapark.wnpserver.domain.webtoon.dto.response;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import lombok.Builder;

@Builder
public record WebtoonSimpleResponse(
        Long id,
        String title,
        String artist,
        String thumbnail
) {

    public static WebtoonSimpleResponse from(Webtoon webtoon) {
        return WebtoonSimpleResponse.builder()
                .id(webtoon.getId())
                .title(webtoon.getTitle())
                .artist(webtoon.getArtist())
                .thumbnail(webtoon.getThumbnail())
                .build();
    }
}
