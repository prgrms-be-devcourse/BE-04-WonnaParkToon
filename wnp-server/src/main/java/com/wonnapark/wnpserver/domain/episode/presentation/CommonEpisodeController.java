package com.wonnapark.wnpserver.domain.episode.presentation;

import com.wonnapark.wnpserver.domain.episode.application.EpisodeReadService;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guest/episode")
@RequiredArgsConstructor
public class GuestEpisodeController implements ReadEpisodeList {

    private final EpisodeReadService episodeReadService;

    @Override
    public ApiResponse<EpisodeDetailFormResponse> episodeDetailFormFind(Long id) {
        EpisodeDetailFormResponse episodeDetailForm = episodeReadService.findEpisodeDetailForm(id);
        return ApiResponse.from(episodeDetailForm);
    }

    @Override
    public ApiResponse<Page<EpisodeListFormResponse>> episodeListFormFind(Long webtoonId, Pageable pageable) {
        Page<EpisodeListFormResponse> episodeListForms = episodeReadService.findEpisodeListForm(webtoonId, pageable);
        return ApiResponse.from(episodeListForms);
    }

}
