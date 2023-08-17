package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.domain.webtoon.dto.request.WebtoonCreateRequest;
import com.wonnapark.wnpserver.domain.webtoon.dto.request.WebtoonUpdateRequest;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonResponse;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonSimpleResponse;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultWebtoonService implements WebtoonService {

    private final WebtoonRepository webtoonRepository;

    @Transactional
    public Long createWebtoon(WebtoonCreateRequest request) {
        Webtoon webtoon = WebtoonCreateRequest.toEntity(request);
        return webtoonRepository.save(webtoon).getId();
    }

    public WebtoonResponse findWebtoonById(Long id) {
        Webtoon webtoon = webtoonRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return WebtoonResponse.from(webtoon);
    }

    public Page<WebtoonSimpleResponse> findWebtoons(Pageable pageable) {
        return webtoonRepository.findAll(pageable)
                .map(WebtoonSimpleResponse::from);
    }

    @Transactional
    public WebtoonResponse updateWebtoon(WebtoonUpdateRequest request, Long id){
        Webtoon webtoon = webtoonRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        webtoon.update(request.title(), request.artist(), request.detail(), request.genre(), request.thumbnail());

        return WebtoonResponse.from(webtoon);
    }

    @Transactional
    public void delete(Long id){
        webtoonRepository.deleteById(id);
    }
}
