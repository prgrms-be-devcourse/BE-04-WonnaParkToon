package com.wonnapark.wnpserver.domain.episode.presentation;

import com.wonnapark.wnpserver.domain.episode.application.EpisodeService;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeCreationResponse;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/admin/episode")
@RequiredArgsConstructor
public class AdminEpisodeController {

    private final EpisodeService episodeService;

    @PostMapping("/{webtoonId}")
    @ResponseStatus(CREATED)
    public ApiResponse<EpisodeCreationResponse> episodeCreate(
            @PathVariable Long webtoonId,
            @RequestBody @Valid EpisodeCreationRequest episodeCreationRequest,
            HttpServletResponse response
    ) {
        Long episodeId = episodeService.createEpisode(webtoonId, episodeCreationRequest);

        String uri = URI.create(String.format("/episode/%d", episodeId)).toString();
        response.setHeader("Location", uri);

        return ApiResponse.from(new EpisodeCreationResponse(episodeId));
    }

    @PatchMapping("/title/{id}")
    @ResponseStatus(OK)
    public void episodeTitleUpdate(@PathVariable Long id, @RequestBody @Valid EpisodeTitleUpdateRequest request) {
        episodeService.updateEpisodeTitle(id, request);
    }

    @PatchMapping("/artist-comment/{id}")
    @ResponseStatus(OK)
    public void episodeArtistCommentUpdate(@PathVariable Long id, @RequestBody @Valid EpisodeArtistCommentUpdateRequest request) {
        episodeService.updateEpisodeArtistComment(id, request);
    }

    @PatchMapping("/thumbnail/{id}")
    @ResponseStatus(OK)
    public void episodeThumbnailUpdate(@PathVariable Long id, @RequestBody @Valid EpisodeThumbnailUpdateRequest request) {
        episodeService.updateEpisodeThumbnail(id, request);
    }

    @PatchMapping("/release-datetime/{id}")
    @ResponseStatus(OK)
    public void episodeReleaseDateTimeUpdate(@PathVariable Long id, @RequestBody @Valid EpisodeReleaseDateTimeUpdateRequest request) {
        episodeService.updateEpisodeReleaseDateTime(id, request);
    }

}
