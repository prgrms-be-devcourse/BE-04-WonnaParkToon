package com.wonnapark.wnpserver.domain.episode.application;

import com.wonnapark.wnpserver.domain.episode.Episode;
import com.wonnapark.wnpserver.domain.episode.EpisodeUrl;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeUrlCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.domain.episode.infrastructure.EpisodeUrlRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultEpisodeService implements EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final EpisodeUrlRepository episodeUrlRepository;

    @Override
    @Transactional
    public Long createEpisode(EpisodeCreationRequest request, List<EpisodeUrlCreationRequest> episodeUrlCreationRequests) {
        Episode newEpisode = EpisodeCreationRequest.toEntity(request);
        List<EpisodeUrl> episodeUrls = episodeUrlCreationRequests.stream()
                .map(EpisodeUrlCreationRequest::toEntity)
                .toList();

        newEpisode.setEpisodeUrls(episodeUrls);
        episodeUrlRepository.saveAll(episodeUrls);
        return episodeRepository.save(newEpisode).getId();
    }

    @Override
    public Page<EpisodeListFormResponse> findEpisodeListForm(Pageable pageable) {
        return episodeRepository.findAll(pageable)
                .map(EpisodeListFormResponse::from);
    }

    @Override
    public EpisodeDetailFormResponse findEpisodeMainForm(Long id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return EpisodeDetailFormResponse.from(episode);
    }

    @Override
    @Transactional
    public void updateEpisodeTitle(Long id, EpisodeTitleUpdateRequest request) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        episode.changeTitle(request.title());
    }

    @Override
    @Transactional
    public void updateEpisodeArtistComment(Long id, EpisodeArtistCommentUpdateRequest request) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        episode.changeArtistComment(request.artistComment());
    }

    @Override
    @Transactional
    public void updateEpisodeThumbnail(Long id, EpisodeThumbnailUpdateRequest request) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        episode.changeThumbNail(request.thumbnail());
    }

    @Override
    @Transactional
    public void updateEpisodeReleaseDateTime(Long id, EpisodeReleaseDateTimeUpdateRequest request) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        episode.changeReleaseDateTime(request.releaseDateTime());
    }

    @Override
    public void deleteEpisode(Long id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        episode.delete();
    }

}
