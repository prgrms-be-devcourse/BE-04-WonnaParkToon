package com.wonnapark.wnpserver.domain.episode;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "episodes")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Episode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "release_date", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private LocalDateTime releaseDateTime;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "artist_comment", nullable = false)
    private String artistComment;

    @ManyToOne
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @OneToMany(mappedBy = "episode")
    List<EpisodeUrl> episodeUrls = new ArrayList<>();

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Builder
    private Episode(String title, LocalDateTime releaseDateTime, String thumbnail, String artistComment) {
        this.title = title;
        this.releaseDateTime = releaseDateTime;
        this.thumbnail = thumbnail;
        this.artistComment = artistComment;
        isDeleted = false;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeArtistComment(String artistComment) {
        this.artistComment = artistComment;
    }

    public void changeThumbNail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void changeReleaseDateTime(LocalDateTime releaseDateTime) {
        this.releaseDateTime = releaseDateTime;
    }

    public void delete() {
        isDeleted = true;
        episodeUrls.forEach(EpisodeUrl::delete);
    }

    public void setEpisodeUrls(List<EpisodeUrl> episodeUrl) {
        for (EpisodeUrl url : episodeUrl) {
            url.setEpisode(this);
        }
    }

}
