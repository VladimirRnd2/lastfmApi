package com.zuzex.lastfm.usercase.album;

import com.zuzex.lastfm.model.AlbumRs;
import com.zuzex.lastfm.model.ArtistRs;

public interface CreateAlbum {

    AlbumRs createAlbum(String name, String mbid, ArtistRs artistRs);
}
