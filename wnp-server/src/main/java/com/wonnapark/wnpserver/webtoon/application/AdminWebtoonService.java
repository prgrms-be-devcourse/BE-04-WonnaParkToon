package com.wonnapark.wnpserver.webtoon.application;

import com.wonnapark.wnpserver.media.S3ManageService;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonCreateRequest;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonUpdateRequest;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
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
    private final S3ManageService s3ManageService;
    private final String THUMBNAIL = "thumbnail";

    @Transactional
    public WebtoonDetailResponse createWebtoon(WebtoonCreateRequest request) throws IOException {
        String key = createFileName(request.title(), request.thumbnail().getOriginalFilename());
        s3ManageService.upload(key, request.thumbnail());
        String thumbnailUrl = s3ManageService.findObjectUrlByName(key);
        Webtoon webtoon = WebtoonCreateRequest.toEntity(request, thumbnailUrl);

        return WebtoonDetailResponse.from(webtoonRepository.save(webtoon));
    }

    @Transactional
    public WebtoonDetailResponse updateWebtoon(WebtoonUpdateRequest request, Long webtoonId) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(WebtoonExceptionMessage.WEBTOON_NOT_FOUND.getMessage(), webtoonId)));
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
     * @param folderName       파일을 저장할 폴더 이름
     * @param originalFileName 파일 원본 이름
     * @return 저장 위치를 포함한 이름
     */
    private String createFileName(String folderName, String originalFileName) {
        String fileExtension = getFileExtension(originalFileName);
        String fileName = THUMBNAIL.concat(fileExtension);
        return folderName + "/" + fileName;
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 파일 형식입니다: %s", fileName));
        }
    }

}
