package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.domain.webtoon.AgeRating;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import org.instancio.Instancio;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import static org.instancio.Select.field;

public final class WebtoonFixtures {

    static final List<String> ageRatingNames = Arrays.stream(AgeRating.values())
            .map(AgeRating::getRatingName).toList();

    private WebtoonFixtures() {
    }

    public static Webtoon createWebtoon() {
        return Instancio.of(Webtoon.class)
                .ignore(field(Webtoon::getIsDeleted))
                .generate(field(Webtoon::getAgeRating), gen -> gen.enumOf(AgeRating.class))
                .create();
    }

    /**
     * 18세 이용가가 아닌 웹툰을 생성하는 메서드
     *
     * @return ageRating이 OVER_18이 아닌 Webtoon 인스턴스
     */
    public static Webtoon createWebtoonUnder18() {
        return Instancio.of(Webtoon.class)
                .ignore(field(Webtoon::getIsDeleted))
                .generate(field(Webtoon::getAgeRating), gen -> gen.enumOf(AgeRating.class).excluding(AgeRating.OVER_18))
                .create();
    }

    /**
     * 18세 이용가 웹툰을 생성하는 메서드
     *
     * @return ageRating이 OVER_18인 Webtoon 인스턴스
     */
    public static Webtoon createWebtoonOver18() {
        return Instancio.of(Webtoon.class)
                .ignore(field(Webtoon::getIsDeleted))
                .set(field(Webtoon::getAgeRating), AgeRating.OVER_18)
                .create();
    }

    public static List<Webtoon> createWebtoons() {
        return Instancio.ofList(Webtoon.class)
                .generate(field(Webtoon::getAgeRating), gen -> gen.enumOf(AgeRating.class))
                .create();
    }

    /**
     * @param publishDay 생성할 웹툰들의 연재 요일
     * @return 인자로 받은 publishDay를 연재 요일로 갖는 웹툰 인스턴스 리스트
     */
    public static List<Webtoon> createWebtoonsOnPublishDay(DayOfWeek publishDay) {
        return Instancio.ofList(Webtoon.class)
                .set(field(Webtoon::getPublishDays), Arrays.asList(publishDay))
                .create();
    }

}
