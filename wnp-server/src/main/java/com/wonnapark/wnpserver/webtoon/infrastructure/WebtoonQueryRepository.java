package com.wonnapark.wnpserver.webtoon.infrastructure;

import java.time.DayOfWeek;
import java.util.List;

import com.wonnapark.wnpserver.webtoon.Webtoon;

public interface WebtoonQueryRepository {

    List<Webtoon> findWebtoonsByPublishDayOrderByLatestViewCount(DayOfWeek publishDay);
    List<Webtoon> findWebtoonsByPublishDayOrderByView(DayOfWeek publishDay);
}
