package com.zuzex.lastfm.usercase.artist;

import com.zuzex.lastfm.model.ArtistRs;

public interface CreateArtist {

    ArtistRs createArtist(String name, String mbid);
}
