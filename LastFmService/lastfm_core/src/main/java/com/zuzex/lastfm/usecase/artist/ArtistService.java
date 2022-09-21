package com.zuzex.lastfm.usecase.artist;

import com.zuzex.lastfm.model.ArtistResponse;

public interface ArtistService {

    ArtistResponse createArtist(String name, String mbid);
}
