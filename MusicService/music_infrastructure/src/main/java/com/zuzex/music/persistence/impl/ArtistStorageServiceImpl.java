package com.zuzex.music.persistence.impl;

import com.zuzex.music.model.Artist;
import com.zuzex.music.persistence.mapper.ArtistMapper;
import com.zuzex.music.persistence.repository.ArtistReactiveRepository;
import com.zuzex.music.usecase.artist.port.ArtistStorageService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ArtistStorageServiceImpl implements ArtistStorageService {

    private final ArtistReactiveRepository artistReactiveRepository;
    private final ArtistMapper mapper;

    @Override
    public Mono<Artist> createArtist(Artist artist) {
        return artistReactiveRepository.save(mapper.mapToEntity(artist)).map(mapper::mapToTable);
    }

    @Override
    public Mono<Artist> getById(Long id) {
        return artistReactiveRepository.findById(id).map(mapper::mapToTable);
    }

    @Override
    public Mono<Artist> getByName(String artistName) {
        return artistReactiveRepository.findByName(artistName).map(mapper::mapToTable);
    }

    @Override
    public Flux<Artist> getAll() {
        return artistReactiveRepository.findAll().map(mapper::mapToTable);
    }
}
