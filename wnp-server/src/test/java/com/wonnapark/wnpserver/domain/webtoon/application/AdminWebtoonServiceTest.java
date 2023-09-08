package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.webtoon.AgeRating;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.webtoon.application.AdminWebtoonService;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonCreateRequest;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonUpdateRequest;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AdminWebtoonServiceTest {

    @InjectMocks
    private AdminWebtoonService adminWebtoonService;
    @Mock
    private WebtoonRepository webtoonRepository;

    @Test
    @DisplayName("올바른 요청을 통해 웹툰을 생성할 수 있다.")
    void createWebtoon() {
        // given
        WebtoonCreateRequest request = WebtoonFixtures.createWebtoonCreateRequest();

        Webtoon webtoon = WebtoonCreateRequest.toEntity(request);
        given(webtoonRepository.save(any(Webtoon.class))).willReturn(webtoon);

        // when
        Long returnedId = adminWebtoonService.createWebtoon(request).id();

        // then
        assertThat(returnedId).isEqualTo(webtoon.getId());

    }

    @Test
    @DisplayName("올바른 요청을 통해 웹툰을 수정할 수 있다.")
    void updateWebtoon() {
        // given
        WebtoonUpdateRequest request = WebtoonFixtures.createWebtoonUpdateRequest();
        Webtoon webtoon = WebtoonFixtures.createWebtoon();
        given(webtoonRepository.findById(any(Long.class))).willReturn(Optional.of(webtoon));

        // when
        adminWebtoonService.updateWebtoon(request, webtoon.getId());

        // then
        assertThat(webtoon.getTitle()).isEqualTo(request.title());
        assertThat(webtoon.getArtist()).isEqualTo(request.artist());
        assertThat(webtoon.getDetail()).isEqualTo(request.detail());
        assertThat(webtoon.getGenre()).isEqualTo(request.genre());
        assertThat(webtoon.getThumbnail()).isEqualTo(request.thumbnail());
        assertThat(webtoon.getPublishDays()).isEqualTo(request.publishDays());
        assertThat(webtoon.getAgeRating()).isEqualTo(AgeRating.from(request.ageRating()));
    }

    @Test
    @DisplayName("웹툰을 삭제할 수 있다.")
    void deleteWebtoon() {
        // given
        Webtoon webtoon = mock(Webtoon.class);
        given(webtoonRepository.findById(any(Long.class))).willReturn(Optional.of(webtoon));

        // when
        adminWebtoonService.deleteWebtoon(webtoon.getId());

        // then
        then(webtoon).should(atMostOnce()).delete();
    }
}
