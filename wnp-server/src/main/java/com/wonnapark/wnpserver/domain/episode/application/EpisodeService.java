package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeUrlCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EpisodeService {

    Long createEpisode(EpisodeCreationRequest request, List<EpisodeUrlCreationRequest> episodeUrlCreationRequests);

    Page<EpisodeListFormResponse> findEpisodeListForm(Pageable pageable);

    EpisodeDetailFormResponse findEpisodeMainForm(Long id);

    void updateEpisodeTitle(Long id, EpisodeTitleUpdateRequest request);

    void updateEpisodeArtistComment(Long id, EpisodeArtistCommentUpdateRequest request);

    void updateEpisodeThumbnail(Long id, EpisodeThumbnailUpdateRequest request);

    void updateEpisodeReleaseDateTime(Long id, EpisodeReleaseDateTimeUpdateRequest request);

    void deleteEpisode(Long id);
}
