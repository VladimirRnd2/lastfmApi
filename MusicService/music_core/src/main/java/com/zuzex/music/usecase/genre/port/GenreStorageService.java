package com.zuzex.music.usecase.genre.port;

import com.zuzex.music.model.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreStorageService {
    Mono<Genre> getById(Long id);

    Mono<Genre> getByName(String name);

    Flux<Genre> getAll();

    Mono<Genre> create(Genre genre);

    Flux<Genre> findAllByTrackId(Long id);
}
