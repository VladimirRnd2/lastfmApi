package com.zuzex.music.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.List;

@Table(name = "track")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackEntity implements Serializable {

    @Id
    private Long id;
    private String name;
    private String mbid;
    private Integer listeners;
    private Integer playcount;
    @Column(value = "artist_id")
    private Long artistId;
    @Column("album_id")
    private Long albumId;
}
