package com.wonnapark.wnpserver.domain.webtoon;

import com.wonnapark.wnpserver.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Webtoon extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_ARTIST_LENGTH = 50;
    private static final int MAX_DETAIL_LENGTH = 200;
    private static final int MAX_GENRE_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = MAX_TITLE_LENGTH)
    private String title;

    @Column(name = "artist", nullable = false, length = MAX_ARTIST_LENGTH)
    private String artist;

    @Column(name = "detail", nullable = false, length = MAX_DETAIL_LENGTH)
    private String detail;

    @Column(name = "genre", nullable = false, length = MAX_GENRE_LENGTH)
    private String genre;

    @Lob
    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "is_deleted", nullable = true)
    private LocalDateTime isDeleted;

    @ElementCollection
    @CollectionTable(name = "publish_day", joinColumns = @JoinColumn(name = "webtoon_id"))
    @Column(name = "publish_days")
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> publishDays = new ArrayList<>();

    @Builder
    private Webtoon(String title, String artist, String detail, String genre, String thumbnail, List<DayOfWeek> publishDays) {
        this.title = title;
        this.artist = artist;
        this.detail = detail;
        this.genre = genre;
        this.thumbnail = thumbnail;
        this.publishDays = new ArrayList<DayOfWeek>(publishDays);
    }

    public void update(String title, String artist, String detail, String genre, String thumbnail) {
        this.title = title;
        this.artist = artist;
        this.detail = detail;
        this.genre = genre;
        this.thumbnail = thumbnail;
    }

}
