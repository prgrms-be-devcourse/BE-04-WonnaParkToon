package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.domain.episode.Episode;
import com.wonnapark.wnpserver.domain.episode.ViewHistory;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.domain.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.domain.episode.infrastructure.ViewHistoryRepository;
import com.wonnapark.wnpserver.domain.user.User;
import com.wonnapark.wnpserver.domain.user.infrastructure.UserRepository;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class EpisodeFindServiceTest {
    @InjectMocks
    EpisodeFindService episodeFindService;
    @Mock
    EpisodeRepository episodeRepository;
    @Mock
    ViewHistoryRepository viewHistoryRepository;
    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("웹툰 ID를 통해 에피소드 리스트 폼을 페이지 조회할 수 있다.")
    void commonFindEpisodeListForm() {
        // given
        User user = makeUser();
        Webtoon webtoon = makeWebtoon();
        Pageable pageable = makePageable();
        List<Episode> episodes = makeEpisodes();
        Page<Episode> page = new PageImpl<>(episodes, pageable, episodes.size());
        given(episodeRepository.findAllByWebtoonId(webtoon.getId(), pageable)).willReturn(page);
        // when
        Page<EpisodeListFormResponse> episodeListForm = episodeFindService.findEpisodeListForm(webtoon.getId(), pageable);
        // then

        assertThat(episodeListForm.getTotalElements()).isEqualTo(Math.min(episodes.size(), pageable.getPageSize()));
    }

    @Test
    @DisplayName("에피소드 ID를 통해 에피소드 디테일 폼을 조회할 수 있다.")
    void commonFindEpisodeDetailForm() {
        // given
        Episode episode = makeOneEpisode();
        given(episodeRepository.findById(anyLong())).willReturn(Optional.of(episode));
        // when
        EpisodeDetailFormResponse episodeDetailForm = episodeFindService.findEpisodeDetailForm(episode.getId());
        // then
        assertThat(episodeDetailForm.id()).isEqualTo(episode.getId());
    }

    @Test
    @DisplayName("유저 ID와 웹툰 ID를 통해 유저가 본 웹툰은 체크된 에피소드 리스트 폼을 페이지 조회할 수 있다.")
    void userFindEpisodeListForm() {
        // given
        User user = makeUser();
        Webtoon webtoon = makeWebtoon();
        Pageable pageable = makePageable();
        List<Episode> episodes = makeEpisodes();
        Page<Episode> page = new PageImpl<>(episodes, pageable, episodes.size());
        given(episodeRepository.findAllByWebtoonId(webtoon.getId(), pageable)).willReturn(page);
        given(viewHistoryRepository.findEpisodeIdByWebtoonIdAndUserId(anyLong(), anyLong())).willReturn(episodes.stream().map(Episode::getId).toList());
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
        User user = makeUser();
        Episode episode = makeOneEpisode();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(episodeRepository.findById(anyLong())).willReturn(Optional.of(episode));
        // when
        EpisodeDetailFormResponse episodeDetailForm = episodeFindService.findEpisodeDetailForm(user.getId(), episode.getId());
        // then
        assertThat(episodeDetailForm.id()).isEqualTo(episode.getId());
    }

    @Test
    @DisplayName("조회 기록이 존재하지 않다면 조회 기록을 저장할 수 있다.")
    void saveViewHistory() {
        // given
        User user = makeUser();
        Episode episode = makeOneEpisode();
        given(viewHistoryRepository.existsByEpisodeIdAndUserId(anyLong(), anyLong())).willReturn(false);
        // when
        episodeFindService.saveViewHistory(user, episode);
        // then
        then(viewHistoryRepository).should(atMostOnce()).save(any(ViewHistory.class));
    }

    @Test
    @DisplayName("조회 기록이 존재하면 조회 기록을 저장하지 않는다.")
    void saveExistingViewHistory() {
        // given
        User user = makeUser();
        Episode episode = makeOneEpisode();
        given(viewHistoryRepository.existsByEpisodeIdAndUserId(anyLong(), anyLong())).willReturn(true);
        // when
        episodeFindService.saveViewHistory(user, episode);
        // then
        then(viewHistoryRepository).should(never()).save(any(ViewHistory.class));
    }

    private Pageable makePageable() {
        return PageRequest.of(0, 10);
    }

    private Episode makeOneEpisode() {
        return Instancio.create(Episode.class);
    }

    private List<Episode> makeEpisodes() {
        return Instancio.ofList(Episode.class).create();
    }

    private User makeUser() {
        return Instancio.create(User.class);
    }

    private Webtoon makeWebtoon() {
        return Instancio.create(Webtoon.class);
    }
}
