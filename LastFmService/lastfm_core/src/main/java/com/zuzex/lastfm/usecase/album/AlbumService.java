package com.zuzex.lastfm.usecase.album;

import com.zuzex.lastfm.model.AlbumResponse;
import com.zuzex.lastfm.model.ArtistResponse;

public interface AlbumService {

    AlbumResponse createAlbum(String name, String mbid, ArtistResponse artistResponse);
}
