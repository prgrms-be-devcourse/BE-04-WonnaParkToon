package com.wonnapark.wnpserver.webtoon.dto.request;

import java.time.DayOfWeek;
import java.util.List;

public record WebtoonUpdateRequest(
        String title,
        String artist,
        String detail,
        String genre,
        String thumbnail,
        String ageRating,
        List<DayOfWeek> publishDays
) {
}


