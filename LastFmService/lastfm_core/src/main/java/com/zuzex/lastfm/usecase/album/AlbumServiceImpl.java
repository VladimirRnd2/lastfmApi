package com.zuzex.lastfm.usecase.album;

import com.zuzex.lastfm.model.AlbumResponse;
import com.zuzex.lastfm.model.ArtistResponse;

public class AlbumServiceImpl implements AlbumService {

    @Override
    public AlbumResponse createAlbum(String name, String mbid, ArtistResponse artistResponse) {
        return AlbumResponse.builder()
                .name(name)
                .mbid(mbid)
                .artist(artistResponse)
                .build();
    }
}
