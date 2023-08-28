package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.domain.episode.Episode;
import com.wonnapark.wnpserver.domain.episode.ViewHistory;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.domain.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.domain.episode.infrastructure.ViewHistoryRepository;
import com.wonnapark.wnpserver.domain.user.User;
import com.wonnapark.wnpserver.domain.user.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wonnapark.wnpserver.domain.episode.application.EpisodeErrorMessage.EPISODE_NOT_FOUND;
import static com.wonnapark.wnpserver.domain.episode.application.EpisodeErrorMessage.USER_NOT_FOUND;

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
        List<Long> viewedEpisodeIds = viewHistoryRepository.findEpisodeIdByWebtoonIdAndUserId(webtoonId, userId);  // 이게 더 빠를거 같은데?? 근데 전체를 조회하는게 맞나??

        return episodes.map(episode -> {
            boolean isViewed = viewedEpisodeIds.contains(episode.getId());
            return EpisodeListFormResponse.from(episode, isViewed);
        });
//        return episodes.map(episode -> {
//            boolean isViewed = viewHistoryRepository.existsByEpisodeIdAndUserId(episode.getId(), userId); // 쿼리가 너무 많이 나감 (N + 1) 쿼리 20개
//            return EpisodeListFormResponse.from(episode, isViewed);
//        });
    }

    @Override
    public EpisodeDetailFormResponse findEpisodeDetailForm(Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EPISODE_NOT_FOUND, episodeId)));

        // TODO: 조회수 처리

        return EpisodeDetailFormResponse.from(episode);
    }

    @Override
    public EpisodeDetailFormResponse findEpisodeDetailForm(Long userId, Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EPISODE_NOT_FOUND, episodeId)));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, userId)));
        saveViewHistory(user, episode);

        // TODO: 조회수 처리

        return EpisodeDetailFormResponse.from(episode);
    }

    @Transactional
    public void saveViewHistory(User user, Episode episode) {
        if (viewHistoryRepository.existsByEpisodeIdAndUserId(episode.getId(), user.getId())) {
            return;
        }
        ViewHistory viewHistory = ViewHistory.builder()
                .user(user)
                .episode(episode)
                .build();
        viewHistoryRepository.save(viewHistory);
    }

}
