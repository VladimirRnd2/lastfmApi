package com.zuzex.music.usecase.genre;

import com.zuzex.music.model.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {

    Mono<Genre> createGenre(Genre genre);

    Mono<Genre> getById(Long id);

    Mono<Genre> getByName(String name);

    Flux<Genre> getAll();

    Flux<Genre> findAllByTrackId(Long id);
}
