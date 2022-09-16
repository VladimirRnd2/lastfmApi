package com.zuzex.music.usecase.album;

import com.zuzex.music.model.Album;
import com.zuzex.music.usecase.album.port.AlbumRepositoryService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CreateAlbumImpl implements CreateAlbum {

    private final AlbumRepositoryService albumRepositoryService;

    @Override
    public Mono<Album> createAlbum(Album album, Long artistId) {
        return albumRepositoryService.createAlbum(album, artistId);
    }
}
