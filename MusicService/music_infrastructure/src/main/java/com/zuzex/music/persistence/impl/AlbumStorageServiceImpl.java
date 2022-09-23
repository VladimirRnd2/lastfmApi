package com.zuzex.music.persistence.impl;

import com.zuzex.music.model.Album;
import com.zuzex.music.persistence.entity.AlbumEntity;
import com.zuzex.music.persistence.mapper.AlbumMapper;
import com.zuzex.music.persistence.repository.AlbumReactiveRepository;
import com.zuzex.music.usecase.album.port.AlbumStorageService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AlbumStorageServiceImpl implements AlbumStorageService {

    private final AlbumReactiveRepository albumReactiveRepository;
    private final AlbumMapper mapper;

    @Override
    public Mono<Album> createAlbum(Album album, Long artistId) {
        AlbumEntity albumEntity = mapper.mapToEntity(album);
        albumEntity.setArtistId(artistId);
        return albumReactiveRepository.save(albumEntity).map(mapper::mapToTable);
    }

    @Override
    public Mono<Album> getById(Long id) {
        return albumReactiveRepository.findById(id).map(mapper::mapToTable);
    }

    @Override
    public Flux<Album> getAll() {
        return albumReactiveRepository.findAll().map(mapper::mapToTable);
    }

    @Override
    public Mono<Album> getByName(String name) {
        return albumReactiveRepository.findByName(name).map(mapper::mapToTable);
    }
}
