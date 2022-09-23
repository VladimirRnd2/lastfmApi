package com.zuzex.music.usecase.album;

import com.zuzex.music.model.Album;
import com.zuzex.music.usecase.album.port.AlbumStorageService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumStorageService albumStorageService;

    @Override
    public Mono<Album> createAlbum(Album album, Long artistId) {
        return albumStorageService.createAlbum(album,artistId);
    }

    @Override
    public Mono<Album> getById(Long id) {
        return albumStorageService.getById(id);
    }

    @Override
    public Flux<Album> getAll() {
        return albumStorageService.getAll();
    }

    @Override
    public Mono<Album> getByName(String name) {
        return albumStorageService.getByName(name);
    }
}
