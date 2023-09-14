package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.ViewCoolTime;
import com.wonnapark.wnpserver.episode.infrastructure.ViewCoolTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.wonnapark.wnpserver.episode.ViewCoolTime.generateKey;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
public class EpisodeViewService {

    private final ViewCoolTimeRepository viewCoolTimeRepository;
    private final ViewHistoryService viewHistoryService;

    @Transactional(propagation = REQUIRES_NEW)
    public void saveViewInfo(Long userId, Episode episode) {
        String key = generateKey(userId, episode.getId());

        if (viewCoolTimeRepository.existsById(key)) {
            return;
        }

        episode.increaseViewCount();
        viewHistoryService.saveViewHistory(userId, episode.getId());
        viewCoolTimeRepository.save(new ViewCoolTime(key, LocalDateTime.now()));
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void saveViewInfo(String ip, Episode episode) {
        String key = generateKey(ip, episode.getId());

        if (viewCoolTimeRepository.existsById(key)) {
            return;
        }

        episode.increaseViewCount();
        viewCoolTimeRepository.save(new ViewCoolTime(key, LocalDateTime.now()));
    }

    public Set<Long> getUserViewedEpisodeIdsInPagedEpisodeIds(Long userId, List<Long> pagedEpisodeIds) {
        return viewHistoryService.findViewedEpisodeIdsForUser(userId, pagedEpisodeIds);
    }

}
