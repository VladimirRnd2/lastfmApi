package com.zuzex.lastfm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackRs {

    private Long id;

    private String name;

    private String mbid;

    private Integer listeners;

    private Integer playcount;

    private ArtistRs artist;

    private AlbumRs album;

    private List<GenreRs> genres;
}
