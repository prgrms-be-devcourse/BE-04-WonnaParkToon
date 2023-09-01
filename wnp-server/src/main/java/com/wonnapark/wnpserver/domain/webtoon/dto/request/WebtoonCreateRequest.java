package com.wonnapark.wnpserver.domain.webtoon.dto.request;

import com.wonnapark.wnpserver.domain.webtoon.AgeRating;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;

import java.time.DayOfWeek;
import java.util.List;

public record WebtoonCreateRequest(
        String title,
        String artist,
        String detail,
        String genre,
        String thumbnail,
        String ageRating,
        List<DayOfWeek> publishDays
) {
    public static Webtoon toEntity(WebtoonCreateRequest request) {
        return Webtoon.builder()
                .title(request.title())
                .artist(request.artist())
                .detail(request.detail())
                .genre(request.genre())
                .thumbnail(request.thumbnail())
                .ageRating(AgeRating.from(request.ageRating()))
                .publishDays(request.publishDays())
                .build();
    }
}
