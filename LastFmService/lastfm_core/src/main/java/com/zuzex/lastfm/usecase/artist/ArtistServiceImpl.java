package com.zuzex.lastfm.usecase.artist;

import com.zuzex.lastfm.model.ArtistResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    @Override
    public ArtistResponse createArtist(String name, String mbid) {
        return ArtistResponse.builder()
                .name(name)
                .mbid(mbid)
                .build();
    }
}
