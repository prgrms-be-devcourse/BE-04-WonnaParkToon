package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.domain.episode.Episode;
import com.wonnapark.wnpserver.domain.episode.ViewHistory;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.domain.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.domain.episode.infrastructure.ViewHistoryRepository;
import com.wonnapark.wnpserver.domain.user.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wonnapark.wnpserver.domain.episode.application.EpisodeErrorMessage.EPISODE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EpisodeFindService implements EpisodeFind {

    private final EpisodeRepository episodeRepository;
    private final ViewHistoryRepository viewHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public Page<EpisodeListFormResponse> findEpisodeListForm(Long webtoonId, Pageable pageable) {
        return episodeRepository.findAllByWebtoonId(webtoonId, pageable)
                .map(EpisodeListFormResponse::from);
    }

    @Override
    public Page<EpisodeListFormResponse> findEpisodeListForm(Long userId, Long webtoonId, Pageable pageable) {
        Page<Episode> episodes = episodeRepository.findAllByWebtoonId(webtoonId, pageable);
        List<Long> episodeIds = episodes.getContent().stream()
                .map(Episode::getId)
                .toList();
        List<Long> viewedEpisodeIds = viewHistoryRepository.findEpisodeIdsInGivenEpisodeIdsByUserId(episodeIds, userId);

        return episodes.map(episode -> {
            boolean isViewed = viewedEpisodeIds.contains(episode.getId());
            return EpisodeListFormResponse.from(episode, isViewed);
        });
    }

    @Override
    public EpisodeDetailFormResponse findEpisodeDetailForm(Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EPISODE_NOT_FOUND.getMessage(), episodeId)));

        // TODO: 조회수 처리

        return EpisodeDetailFormResponse.from(episode);
    }

    @Override
    public EpisodeDetailFormResponse findEpisodeDetailForm(Long userId, Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EPISODE_NOT_FOUND.getMessage(), episodeId)));

        saveViewHistory(userId, episodeId);

        // TODO: 조회수 처리

        return EpisodeDetailFormResponse.from(episode);
    }

    @Transactional
    public void saveViewHistory(Long userId, Long episodeId) {
        if (viewHistoryRepository.existsByUserIdAndEpisodeId(userId, episodeId)) {
            return;
        }
        ViewHistory viewHistory = ViewHistory.builder()
                .userId(userId)
                .episodeId(episodeId)
                .build();
        viewHistoryRepository.save(viewHistory);
    }

}
