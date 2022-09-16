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
public class AlbumRs {

    private Long id;

    private String name;

    private String mbid;

    private List<TrackRs> trackList;

    private ArtistRs artist;
}
