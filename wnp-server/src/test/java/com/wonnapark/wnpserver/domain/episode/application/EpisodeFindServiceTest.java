package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.application.EpisodeFindService;
import com.wonnapark.wnpserver.episode.application.ViewHistoryService;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.user.User;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.wonnapark.wnpserver.domain.episode.EpisodeFixtures.createEpisode;
import static com.wonnapark.wnpserver.domain.episode.EpisodeFixtures.createEpisodes;
import static com.wonnapark.wnpserver.domain.episode.EpisodeFixtures.createPageable;
import static com.wonnapark.wnpserver.domain.episode.EpisodeFixtures.createUser;
import static com.wonnapark.wnpserver.domain.episode.EpisodeFixtures.createWebtoon;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EpisodeFindServiceTest {
    @InjectMocks
    private EpisodeFindService episodeFindService;
    @Mock
    private EpisodeRepository episodeRepository;
    @Mock
    private ViewHistoryService viewHistoryService;
    private Webtoon webtoon;

    @BeforeEach
    void init() {
        webtoon = createWebtoon();
    }

    @Test
    @DisplayName("웹툰 ID를 통해 에피소드 리스트 폼을 페이지 조회할 수 있다.")
    void commonFindEpisodeListForm() {
        // given
        Pageable pageable = createPageable();
        List<Episode> episodes = createEpisodes(webtoon).subList(0, pageable.getPageSize());
        Page<Episode> page = new PageImpl<>(episodes, pageable, episodes.size());
        given(episodeRepository.findAllByWebtoonId(webtoon.getId(), pageable)).willReturn(page);
        // when
        Page<EpisodeListFormResponse> episodeListForm = episodeFindService.findEpisodeListForm(webtoon.getId(), pageable);
        // then
        assertThat(episodeListForm.getNumberOfElements()).isEqualTo(Math.min(episodes.size(), pageable.getPageSize()));
    }

    @Test
    @DisplayName("에피소드 ID를 통해 에피소드 디테일 폼을 조회할 수 있다.")
    void commonFindEpisodeDetailForm() {
        // given
        Long episodeId = Instancio.create(Long.class);
        Episode episode = createEpisode(webtoon);
        given(episodeRepository.findById(anyLong())).willReturn(Optional.of(episode));
        // when
        EpisodeDetailFormResponse episodeDetailForm = episodeFindService.findEpisodeDetailForm(episodeId);
        // then
        assertThat(episodeDetailForm.title()).isEqualTo(episode.getTitle());
    }

    @Test
    @DisplayName("유저 ID와 웹툰 ID를 통해 이미 본 에피소드에 대한 정보를 포함한 에피소드 리스트를 페이지 조회할 수 있다.")
    void userFindEpisodeListForm() {
        // given
        User user = createUser();
        Pageable pageable = createPageable();
        List<Episode> episodes = createEpisodes(webtoon).subList(0, pageable.getPageSize());
        Page<Episode> page = new PageImpl<>(episodes, pageable, episodes.size());
        given(episodeRepository.findAllByWebtoonId(webtoon.getId(), pageable)).willReturn(page);
        given(viewHistoryService.findViewedEpisodeIdsForUser(
                        user.getId(),
                        episodes.stream()
                                .map(Episode::getId)
                                .toList()
                )
        ).willReturn(
                episodes.stream()
                        .map(Episode::getId)
                        .toList()
        );
        // when
        Page<EpisodeListFormResponse> episodeListForm = episodeFindService.findEpisodeListForm(user.getId(), webtoon.getId(), pageable);
        // then
        assertThat(episodeListForm.getContent()).isNotEmpty();
        assertThat(episodeListForm.getContent()).allMatch(EpisodeListFormResponse::isViewed);
        assertThat(episodeListForm.getTotalElements()).isEqualTo(Math.min(episodes.size(), pageable.getPageSize()));
    }

    @Test
    @DisplayName("유저 ID와 에피소드 ID를 통해 에피소드 디테일 폼을 조회할 수 있다.")
    void userFindEpisodeDetailForm() {
        // given
        User user = createUser();
        Episode episode = createEpisode(webtoon);
        given(episodeRepository.findById(episode.getId())).willReturn(Optional.of(episode));
        // when
        EpisodeDetailFormResponse episodeDetailForm = episodeFindService.findEpisodeDetailForm(user.getId(), episode.getId());
        // then
        assertThat(episodeDetailForm.id()).isEqualTo(episode.getId());
    }

}
