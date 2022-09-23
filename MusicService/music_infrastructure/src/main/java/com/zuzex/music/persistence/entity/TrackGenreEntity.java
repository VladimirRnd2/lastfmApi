package com.zuzex.music.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "track_genre")
public class TrackGenreEntity {

    @Id
    private Long id;

    @Column(value = "track_id")
    private Long trackId;

    @Column(value = "genre_id")
    private Long genreId;
}
