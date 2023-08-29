package com.wonnapark.wnpserver.domain.episode.infrastructure;

import com.wonnapark.wnpserver.domain.episode.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {

    @Query("SELECT COUNT(v) > 0 FROM ViewHistory v WHERE v.id.userId = :userId AND v.id.episodeId = :episodeId")
    boolean existsByUserIdAndEpisodeId(Long userId, Long episodeId);

    @Query("SELECT v.id.episodeId FROM ViewHistory v WHERE v.id.userId = :userId AND v.id.episodeId IN :episodeIds")
    List<Long> findEpisodeIdsInGivenEpisodeIdsByUserId(List<Long> episodeIds, Long userId);

}
