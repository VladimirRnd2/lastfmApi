package com.zuzex.music.usecase.album;

import com.zuzex.music.model.Album;
import reactor.core.publisher.Mono;

public interface CreateAlbum {
    Mono<Album> createAlbum(Album album, Long artistId);
}
