package com.zuzex.music.usecase.genre;

import com.zuzex.music.model.Genre;
import com.zuzex.music.usecase.genre.port.GenreRepositoryService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class FindGenreImpl implements FindGenre {

    private final GenreRepositoryService genreRepositoryService;

    @Override
    public Mono<Genre> getById(Long id) {
        return genreRepositoryService.getById(id);
    }

    @Override
    public Mono<Genre> getByName(String name) {
        return genreRepositoryService.getByName(name);
    }

    @Override
    public Flux<Genre> getAll() {
        return genreRepositoryService.getAll();
    }

    @Override
    public Flux<Genre> findAllByTrackId(Long id) {
        return genreRepositoryService.findAllByTrackId(id);
    }
}
