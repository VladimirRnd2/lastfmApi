package com.zuzex.music.usecase.album;

import com.zuzex.music.model.Album;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlbumService {

    Mono<Album> createAlbum(Album album, Long artistId);

    Mono<Album> getById(Long id);

    Flux<Album> getAll();

    Mono<Album> getByName(String name);
}
