package com.wonnapark.wnpserver.domain.webtoon.dto.response;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;

import java.time.DayOfWeek;
import java.util.List;

public record WebtoonResponse(
        Long id,
        String title,
        String artist,
        String detail,
        String genre,
        String thumbnail,
        Integer ageLimit,
        List<DayOfWeek> publishDays
) {
    public static WebtoonResponse from(Webtoon webtoon) {
        return new WebtoonResponse(
                webtoon.getId(),
                webtoon.getTitle(),
                webtoon.getArtist(),
                webtoon.getDetail(),
                webtoon.getGenre(),
                webtoon.getThumbnail(),
                webtoon.getAgeLimit(),
                webtoon.getPublishDays()
        );
    }

}
