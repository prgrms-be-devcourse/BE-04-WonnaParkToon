package com.wonnapark.wnpserver.webtoon.dto.request;

import com.wonnapark.wnpserver.webtoon.AgeRating;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.util.List;

public record WebtoonCreateRequest(
        String title,
        String artist,
        String detail,
        String genre,
        MultipartFile thumbnail,
        String ageRating,
        List<DayOfWeek> publishDays
) {
    public static Webtoon toEntity(WebtoonCreateRequest request, String thumbnailUrl) {
        return Webtoon.builder()
                .title(request.title())
                .artist(request.artist())
                .detail(request.detail())
                .genre(request.genre())
                .thumbnail(thumbnailUrl)
                .ageRating(AgeRating.from(request.ageRating()))
                .publishDays(request.publishDays())
                .build();
    }
}
