package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.domain.episode.ViewHistory;
import com.wonnapark.wnpserver.domain.episode.infrastructure.ViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ViewHistoryService {

    private final ViewHistoryRepository viewHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveViewHistory(Long userId, Long episodeId) {
        if (viewHistoryRepository.existsById_UserIdAndId_EpisodeId(userId, episodeId)) {
            return;
        }
        ViewHistory viewHistory = ViewHistory.builder()
                .userId(userId)
                .episodeId(episodeId)
                .build();
        viewHistoryRepository.save(viewHistory);
    }

    public List<Long> findViewedEpisodeIdsForUser(Long userId, List<Long> episodeIds) {
        return viewHistoryRepository.findEpisodeIdsInGivenEpisodeIdsByUserId(userId, episodeIds);
    }

}
