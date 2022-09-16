package com.zuzex.music.persistence.impl;

import com.zuzex.music.model.Genre;
import com.zuzex.music.persistence.mapper.GenreRepositoryMapper;
import com.zuzex.music.persistence.repository.GenreReactiveRepository;
import com.zuzex.music.usecase.genre.port.GenreRepositoryService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GenreRepositoryServiceImpl implements GenreRepositoryService {

    private final GenreReactiveRepository genreReactiveRepository;
    private final GenreRepositoryMapper mapper;

    @Override
    public Mono<Genre> getById(Long id) {
        return genreReactiveRepository.findById(id).map(mapper::mapToTable);
    }

    @Override
    public Mono<Genre> getByName(String name) {
        return genreReactiveRepository.findByName(name).map(mapper::mapToTable);
    }

    @Override
    public Flux<Genre> getAll() {
        return genreReactiveRepository.findAll().map(mapper::mapToTable);
    }

    @Override
    public Mono<Genre> create(Genre genre) {
        return genreReactiveRepository.save(mapper.mapToEntity(genre)).map(mapper::mapToTable);
    }
    @Override
    public Flux<Genre> findAllByTrackId(Long id) {
        return genreReactiveRepository.findAllByTrackId(id).map(mapper::mapToTable);
    }
}
