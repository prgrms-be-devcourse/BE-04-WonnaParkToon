package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.domain.webtoon.dto.request.WebtoonCreateRequest;
import com.wonnapark.wnpserver.domain.webtoon.dto.request.WebtoonUpdateRequest;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonResponse;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WebtoonService {
    Long createWebtoon(WebtoonCreateRequest request);
    WebtoonResponse findWebtoonById(Long id);
    Page<WebtoonSimpleResponse> findWebtoons(Pageable pageable);
    WebtoonResponse updateWebtoon(WebtoonUpdateRequest request, Long id);
    void delete(Long id);
}
