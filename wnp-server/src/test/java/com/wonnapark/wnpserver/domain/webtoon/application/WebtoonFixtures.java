package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.domain.webtoon.AgeRating;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import org.instancio.Instancio;

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

}
