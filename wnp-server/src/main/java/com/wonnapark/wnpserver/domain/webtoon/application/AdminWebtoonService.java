package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.dto.request.WebtoonCreateRequest;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
