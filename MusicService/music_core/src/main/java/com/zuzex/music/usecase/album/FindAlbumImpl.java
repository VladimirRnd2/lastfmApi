package com.zuzex.music.usecase.album;

import com.zuzex.music.model.Album;
import com.zuzex.music.usecase.album.port.AlbumRepositoryService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class FindAlbumImpl implements FindAlbum {

    private final AlbumRepositoryService albumRepositoryService;

    @Override
    public Mono<Album> getById(Long id) {
        return albumRepositoryService.getById(id);
    }

    @Override
    public Flux<Album> getAll() {
        return albumRepositoryService.getAll();
    }

    @Override
    public Mono<Album> getByName(String name) {
        return albumRepositoryService.getByName(name);
    }
}
