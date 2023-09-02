package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonSimpleResponse;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultWebtoonServiceTest {

    @InjectMocks
    private DefaultWebtoonService defaultWebtoonService;
    @Mock
    private WebtoonRepository webtoonRepository;

    @Test
    void findAllWebtoonsWithPaging() {

    }

    @Test
    void findWebtoonsByPublishDay() {
        // given
        Map<DayOfWeek, List<Webtoon>> webtoonsOnDayOfWeek = new HashMap<>();
        for (DayOfWeek publishDay : DayOfWeek.values()) {
            webtoonsOnDayOfWeek.put(publishDay, WebtoonFixtures.createWebtoonsOnPublishDay(publishDay));
            given(webtoonRepository.findByPublishDaysContains(eq(publishDay))).willReturn(webtoonsOnDayOfWeek.get(publishDay));
        }

        // when
        Map<DayOfWeek, List<WebtoonSimpleResponse>> responsesOnDayOfWeek = new HashMap<>();
        for (DayOfWeek publishDay : DayOfWeek.values()) {
            responsesOnDayOfWeek.put(publishDay, defaultWebtoonService.findWebtoonsByPublishDay(publishDay));
        }

        // then
        for (DayOfWeek publishDay : DayOfWeek.values()) {
            assertThat(responsesOnDayOfWeek.get(publishDay).size()).isEqualTo(webtoonsOnDayOfWeek.get(publishDay).size());
            assertThat(responsesOnDayOfWeek.get(publishDay)).isEqualTo(
                    webtoonsOnDayOfWeek.get(publishDay).stream().map(WebtoonSimpleResponse::from).toList()
            );
        }
    }
}
