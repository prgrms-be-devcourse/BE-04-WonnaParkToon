package com.wonnapark.wnpserver.webtoon.application;

import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonDetailRequest;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminWebtoonService {

    private final WebtoonRepository webtoonRepository;

    @Transactional
    public WebtoonDetailResponse createWebtoon(WebtoonDetailRequest request) {
        Webtoon webtoon = WebtoonDetailRequest.toEntity(request);
        return WebtoonDetailResponse.from(webtoonRepository.save(webtoon));
    }

    @Transactional
    public WebtoonDetailResponse updateWebtoon(WebtoonDetailRequest request, Long webtoonId) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(WebtoonExceptionMessage.WEBTOON_NOT_FOUND.getMessage(), webtoonId)));
        webtoon.changeDetail(
                request.title(),
                request.artist(),
                request.summary(),
                request.genre(),
                request.ageRating(),
                request.publishDays()
        );

        return WebtoonDetailResponse.from(webtoon);
    }

    @Transactional
    public LocalDateTime deleteWebtoon(Long webtoonId) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(WebtoonExceptionMessage.WEBTOON_NOT_FOUND.getMessage(), webtoonId)));
        webtoon.delete();

        return webtoon.getIsDeleted();
    }

}
