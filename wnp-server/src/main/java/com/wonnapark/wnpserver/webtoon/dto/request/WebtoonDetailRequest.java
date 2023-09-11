package com.wonnapark.wnpserver.webtoon.dto.request;

import com.wonnapark.wnpserver.webtoon.AgeRating;
import com.wonnapark.wnpserver.webtoon.Webtoon;

import java.time.DayOfWeek;
import java.util.List;

public record WebtoonDetailRequest(
        String title,
        String artist,
        String detail,
        String genre,
        String ageRating,
        List<DayOfWeek> publishDays
) {
    public static Webtoon toEntity(WebtoonDetailRequest request) {
        return Webtoon.builder()
                .title(request.title())
                .artist(request.artist())
                .detail(request.detail())
                .genre(request.genre())
                .ageRating(AgeRating.from(request.ageRating()))
                .publishDays(request.publishDays())
                .build();
    }
}
