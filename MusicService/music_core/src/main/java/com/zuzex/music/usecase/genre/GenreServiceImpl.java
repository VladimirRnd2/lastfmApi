package com.zuzex.music.usecase.genre;

import com.zuzex.music.model.Genre;
import com.zuzex.music.usecase.genre.port.GenreStorageService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreStorageService genreStorageService;

    @Override
    public Mono<Genre> createGenre(Genre genre) {
        return genreStorageService.create(genre);
    }

    @Override
    public Mono<Genre> getById(Long id) {
        return genreStorageService.getById(id);
    }

    @Override
    public Mono<Genre> getByName(String name) {
        return genreStorageService.getByName(name);
    }

    @Override
    public Flux<Genre> getAll() {
        return genreStorageService.getAll();
    }

    @Override
    public Flux<Genre> findAllByTrackId(Long id) {
        return genreStorageService.findAllByTrackId(id);
    }
}
