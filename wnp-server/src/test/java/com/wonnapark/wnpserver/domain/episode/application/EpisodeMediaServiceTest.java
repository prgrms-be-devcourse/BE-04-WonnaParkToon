package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.episode.application.EpisodeMediaService;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeMediaUploadResponse;
import com.wonnapark.wnpserver.media.S3MediaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EpisodeMediaServiceTest {

    @InjectMocks
    EpisodeMediaService episodeMediaService;
    @Mock
    S3MediaService s3MediaService;

    @Test
    void uploadEpisodeMedia() {
        // given
        String webtoonId = "1";
        MultipartFile thumbnail = new MockMultipartFile("thumbnail", "thumbnail".getBytes());
        List<MultipartFile> episodeImages = new ArrayList<>(Arrays.asList(
                new MockMultipartFile("image1", "image1".getBytes()),
                new MockMultipartFile("image2", "image2".getBytes()),
                new MockMultipartFile("image3", "image3".getBytes()),
                new MockMultipartFile("image4", "image4".getBytes()),
                new MockMultipartFile("image5", "image5".getBytes())
        ));
        String expectedThumbnailUrl = "https://sample.thumbnail.jpeg";
        List<String> expectedImageUrls = IntStream.range(1, 6)
                .mapToObj(operand -> String.format("https://sample.image_%d.jpeg", operand))
                .toList();

        given(s3MediaService.upload(anyString(), eq(thumbnail))).willReturn(expectedThumbnailUrl);
        IntStream.range(0, 5).forEach(i ->
                given(s3MediaService.upload(anyString(), eq(episodeImages.get(i)))).willReturn(expectedImageUrls.get(i))
        );

        // when
        EpisodeMediaUploadResponse episodeMediaUploadResponse = episodeMediaService.uploadEpisodeMedia(webtoonId, thumbnail, episodeImages);

        // then
        assertThat(episodeMediaUploadResponse.thumbnailUrl()).isEqualTo(expectedThumbnailUrl);
        assertThat(episodeMediaUploadResponse.urls()).isEqualTo(expectedImageUrls);
        then(s3MediaService).should().upload(anyString(), eq(thumbnail));
        IntStream.range(0, 5).forEach(i ->
                then(s3MediaService).should().upload(anyString(), eq(episodeImages.get(i)))
        );
    }

}
