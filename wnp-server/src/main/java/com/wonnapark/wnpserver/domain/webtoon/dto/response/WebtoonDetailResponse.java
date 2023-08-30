package com.wonnapark.wnpserver.domain.webtoon.dto.response;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;

import java.time.DayOfWeek;
import java.util.List;

public record WebtoonDetailResponse(
        Long id,
        String title,
        String artist,
        String detail,
        String genre,
        String thumbnail,
        String ageRating,
        List<DayOfWeek> publishDays
) {

    public static WebtoonDetailResponse from(Webtoon webtoon) {
        return new WebtoonDetailResponse(
                webtoon.getId(),
                webtoon.getTitle(),
                webtoon.getArtist(),
                webtoon.getDetail(),
                webtoon.getGenre(),
                webtoon.getThumbnail(),
                webtoon.getAgeRating().getRatingName(),
                List.copyOf(webtoon.getPublishDays())
        );
    }
}
