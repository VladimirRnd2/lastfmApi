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
public class TrackResponse {

    private Long id;

    private String name;

    private String mbid;

    private Integer listeners;

    private Integer playcount;

    private ArtistResponse artist;

    private AlbumResponse album;

    private List<GenreResponse> genres;
}
