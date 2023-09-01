package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.dto.request.WebtoonCreateRequest;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminWebtoonServiceTest {

    private final List<String> ageRatingNames = Arrays.asList("전체이용가", "12세이용가", "15세이용가", "18세이용가");
    @InjectMocks
    private AdminWebtoonService adminWebtoonService;
    @Mock
    private WebtoonRepository webtoonRepository;

    @Test
    @DisplayName("올바른 요청을 통해 웹툰을 생성할 수 있다.")
    void createWebtoon() {
        // given
        WebtoonCreateRequest request = Instancio.of(WebtoonCreateRequest.class)
                .generate(field(WebtoonCreateRequest::ageRating), gen -> gen.oneOf(ageRatingNames))
                .create();

        Webtoon webtoon = WebtoonCreateRequest.toEntity(request);
        given(webtoonRepository.save(any(Webtoon.class))).willReturn(webtoon);

        // when
        Long returnedId = adminWebtoonService.createWebtoon(request).id();

        // then
        assertThat(returnedId).isEqualTo(webtoon.getId());

    }

}
