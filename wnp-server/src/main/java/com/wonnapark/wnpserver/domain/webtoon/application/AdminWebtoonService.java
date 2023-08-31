package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.dto.request.WebtoonCreateRequest;
import com.wonnapark.wnpserver.domain.webtoon.dto.request.WebtoonUpdateRequest;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wonnapark.wnpserver.domain.webtoon.application.WebtoonExceptionMessage.WEBTOON_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminWebtoonService {

    private final WebtoonRepository webtoonRepository;

    @Transactional
    public Long createWebtoon(WebtoonCreateRequest request) {
        Webtoon webtoon = WebtoonCreateRequest.toEntity(request);
        return webtoonRepository.save(webtoon).getId();
    }

    @Transactional
    public WebtoonDetailResponse updateWebtoon(WebtoonUpdateRequest request, Long webtoonId) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(WEBTOON_NOT_FOUND.getMessage(), webtoonId)));
        webtoon.change(
                request.title(),
                request.artist(),
                request.detail(),
                request.genre(),
                request.thumbnail(),
                request.ageRating(),
                request.publishDays()
        );

        return WebtoonDetailResponse.from(webtoon);
    }

}
