package com.wonnapark.wnpserver.webtoon.application;

import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonCreateDetailRequest;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonUpdateDetailRequest;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonThumbnailResponse;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminWebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final S3MediaService s3MediaService;
    private final String DEFAULT_WEBTOON_THUMBNAIL = "https://wonnapark-bucket.s3.ap-northeast-2.amazonaws.com/webtoon/thumbnail_default.jpg";
    private final String WEBTOON_THUMBNAIL_PATTERN = "webtoon/%d/thumbnail/thumbnail_IMAG21_%s.%s";

    @Transactional
    public WebtoonDetailResponse createWebtoonDetail(WebtoonCreateDetailRequest request) {
        Webtoon webtoon = WebtoonCreateDetailRequest.toEntity(request, DEFAULT_WEBTOON_THUMBNAIL);
        return WebtoonDetailResponse.from(webtoonRepository.save(webtoon));
    }

    @Transactional
    public WebtoonDetailResponse updateWebtoonDetail(WebtoonUpdateDetailRequest request, Long webtoonId) {
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

    /**
     * [웹툰 작품명/thumbnail.확장자] 형식으로 파일 이름을 생성
     *
     * @param webtoonId        웹툰 ID
     * @param originalFileName 파일 원본 이름
     * @return 저장 위치를 포함한 이름
     */
    private String createWebtoonThumbnailKey(Long webtoonId, String originalFileName) {
        String fileExtension = getFileExtension(originalFileName);
        return String.format(WEBTOON_THUMBNAIL_PATTERN, webtoonId, UUID.randomUUID(), fileExtension);
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 파일 형식입니다: %s", fileName));
        }
    }

}
