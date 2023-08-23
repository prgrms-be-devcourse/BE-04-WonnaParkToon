package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.domain.episode.Episode;
import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeUrlCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.domain.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class DefaultEpisodeServiceTest {

    @InjectMocks
    private DefaultEpisodeService episodeService;
    @Mock
    private EpisodeRepository episodeRepository;
    @Mock
    private WebtoonRepository webtoonRepository;

    @Test
    @DisplayName("에피소드를 생성할 수 있다.")
    void testCreateEpisode() {
        // given
        List<EpisodeUrlCreationRequest> episodeUrlCreationRequests = Instancio.ofList(EpisodeUrlCreationRequest.class).create();
        EpisodeCreationRequest episodeCreationRequest = Instancio.of(EpisodeCreationRequest.class)
                .set(field(EpisodeCreationRequest::episodeUrlCreationRequests), episodeUrlCreationRequests)
                .create();
        Webtoon webtoon = Instancio.create(Webtoon.class);
        Episode episode = createEpisodeByRequest(episodeCreationRequest);
        given(webtoonRepository.findById(any(Long.class))).willReturn(Optional.of(webtoon));
        given(episodeRepository.save(any(Episode.class))).willReturn(episode);

        // when
        Long returnedId = episodeService.createEpisode(webtoon.getId(), episodeCreationRequest);

        // then
        assertThat(returnedId).isEqualTo(episode.getId());
    }

    private Episode createEpisodeByRequest(EpisodeCreationRequest episodeCreationRequest) {
        return Instancio.of(Episode.class)
                .set(field(Episode::getTitle), episodeCreationRequest.title())
                .set(field(Episode::getArtistComment), episodeCreationRequest.artistComment())
                .set(field(Episode::getReleaseDateTime), episodeCreationRequest.releaseDateTime())
                .set(field(Episode::getThumbnail), episodeCreationRequest.thumbnail())
                .set(field(Episode::getEpisodeUrls), episodeCreationRequest.episodeUrlCreationRequests().stream().map(EpisodeUrlCreationRequest::toEntity).toList())
                .create();
    }

    @Test
    @DisplayName("에피소드 리스트 폼을 페이지로 조회할 수 있다.")
    void testFindEpisodeListForm() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Episode> responses = Instancio.ofList(Episode.class).create();
        Page<Episode> page = new PageImpl<>(responses, pageable, responses.size());
        Webtoon webtoon = Instancio.create(Webtoon.class);
        given(episodeRepository.findAllById(any(Long.class), any(Pageable.class))).willReturn(page);

        // when
        Page<EpisodeListFormResponse> episodeListForm = episodeService.findEpisodeListForm(webtoon.getId(), pageable);

        // then
        assertThat(episodeListForm.getTotalElements()).isEqualTo(responses.size());
    }

    @Test
    @DisplayName("에피소드 메인 폼을 조회할 수 있다.")
    void testFindEpisodeMainForm() {
        // given
        Episode episode = Instancio.create(Episode.class);
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        EpisodeDetailFormResponse episodeMainForm = episodeService.findEpisodeMainForm(episode.getId());

        // then
        assertThat(episode.getId()).isEqualTo(episodeMainForm.id());
    }

    @Test
    @DisplayName("에피소드 타이틀을 수정할 수 있다.")
    void testUpdateEpisodeTitle() {
        // given
        Episode episode = mock(Episode.class);
        Long episodeId = episode.getId();
        EpisodeTitleUpdateRequest request = new EpisodeTitleUpdateRequest(Instancio.create(String.class));
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        episodeService.updateEpisodeTitle(episodeId, request);

        // then
        then(episode).should(atMostOnce()).changeTitle(request.title());
    }


    @Test
    @DisplayName("에피소드 작가의 말을 수정할 수 있다.")
    void testUpdateEpisodeArtistComment() {
        // given
        Episode episode = mock(Episode.class);
        Long episodeId = episode.getId();
        EpisodeArtistCommentUpdateRequest request = new EpisodeArtistCommentUpdateRequest(Instancio.create(String.class));
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        episodeService.updateEpisodeArtistComment(episodeId, request);

        // then
        then(episode).should(atMostOnce()).changeArtistComment(request.artistComment());
    }

    @Test
    @DisplayName("에피소드 썸네일을 바꿀 수 있다.")
    void testUpdateEpisodeThumbnail() {
        // given
        Episode episode = mock(Episode.class);
        Long episodeId = episode.getId();
        EpisodeThumbnailUpdateRequest request = new EpisodeThumbnailUpdateRequest(Instancio.create(String.class));
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        episodeService.updateEpisodeThumbnail(episodeId, request);

        // then
        then(episode).should(atMostOnce()).changeThumbNail(request.thumbnail());
    }

    @Test
    @DisplayName("에피소드 공개일을 바꿀 수 있다.")
    void testUpdateEpisodeReleaseDateTime() {
        // given
        Episode episode = mock(Episode.class);
        Long episodeId = episode.getId();
        EpisodeReleaseDateTimeUpdateRequest request = new EpisodeReleaseDateTimeUpdateRequest(Instancio.create(LocalDateTime.class));
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        episodeService.updateEpisodeReleaseDateTime(episodeId, request);

        // then
        then(episode).should(atMostOnce()).changeReleaseDateTime(request.releaseDateTime());
    }

    @Test
    @DisplayName("에피소드를 삭제할 수 있다.")
    void testDeleteEpisode() {
        // given
        List<EpisodeUrl> episodeUrls = Instancio.ofList(EpisodeUrl.class).create();
        Episode episode = Instancio.of(Episode.class)
                .set(field(Episode::getEpisodeUrls), episodeUrls)
                .create();
        Long episodeId = episode.getId();
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        episodeService.deleteEpisode(episodeId);

        // then
        assertThat(episode.isDeleted()).isTrue();
        assertThat(episodeUrls.stream().allMatch(EpisodeUrl::isDeleted)).isTrue();
    }

}
