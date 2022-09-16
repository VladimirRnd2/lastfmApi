package com.zuzex.lastfm.usercase.artist;

import com.zuzex.lastfm.model.ArtistRs;

public class CreateArtistImpl implements CreateArtist {

    @Override
    public ArtistRs createArtist(String name, String mbid) {
        return ArtistRs.builder().name(name).mbid(mbid).build();
    }
}
