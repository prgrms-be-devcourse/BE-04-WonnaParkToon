package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.ViewCoolTime;
import com.wonnapark.wnpserver.episode.infrastructure.ViewCoolTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EpisodeViewService {

    private final ViewCoolTimeRepository viewCoolTimeRepository;
    private final ViewHistoryService viewHistoryService;

    @Transactional
    public boolean saveViewInfo(Long userId, Long episodeId) {
        String key = generateKey(userId, episodeId);

        if (viewCoolTimeRepository.existsById(key)) {
            return false;
        }

        viewHistoryService.saveViewHistory(userId, episodeId);
        viewCoolTimeRepository.save(new ViewCoolTime(key, LocalDateTime.now()));
        return true;
    }

    public boolean saveViewInfo(String ip, Long episodeId) {
        String key = generateKey(ip, episodeId);

        if (viewCoolTimeRepository.existsById(key)) {
            return false;
        }

        viewCoolTimeRepository.save(new ViewCoolTime(key, LocalDateTime.now()));
        return true;
    }

    public Set<Long> getUserViewedEpisodeIdsInPagedEpisodeIds(Long userId, List<Long> pagedEpisodeIds) {
        return viewHistoryService.findViewedEpisodeIdsForUser(userId, pagedEpisodeIds);
    }

    private String generateKey(Object prefix, Object postfix) {
        return String.format("%s:%s", prefix, postfix);
    }

}
