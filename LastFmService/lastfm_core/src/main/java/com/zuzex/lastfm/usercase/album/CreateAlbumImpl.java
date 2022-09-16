package com.zuzex.lastfm.usercase.album;

import com.zuzex.lastfm.model.AlbumRs;
import com.zuzex.lastfm.model.ArtistRs;

public class CreateAlbumImpl implements CreateAlbum {
    @Override
    public AlbumRs createAlbum(String name, String mbid, ArtistRs artistRs) {
        return AlbumRs.builder().name(name).mbid(mbid).artist(artistRs).build();
    }
}
