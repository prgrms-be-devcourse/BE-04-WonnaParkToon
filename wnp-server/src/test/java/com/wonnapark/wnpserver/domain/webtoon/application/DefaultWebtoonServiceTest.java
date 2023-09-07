package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonSimpleResponse;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    @DisplayName("전체 웹툰을 페이지 조회할 수 있다.")
    void findAllWebtoonsWithPaging() {
        // given
        Pageable pageable = PageRequest.of(0, 20, Sort.by("title").ascending());
        List<Webtoon> webtoons = WebtoonFixtures.createWebtoons();
        Page<Webtoon> page = new PageImpl<>(webtoons, pageable, webtoons.size());
        given(webtoonRepository.findAll(pageable)).willReturn(page);

        // when
        Page<WebtoonSimpleResponse> response = defaultWebtoonService.findAllWebtoonsWithPaging(pageable);

        // then
        assertThat(response.getTotalElements()).isEqualTo(Math.min(webtoons.size(), pageable.getPageSize()));
    }

    @Test
    @DisplayName("요일별 웹툰 목록을 조회할 수 있다.")
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

    // TODO: 2023-09-03 findAllWebtoonsForEachDayOfWeek 테스트 코드 추가

}
