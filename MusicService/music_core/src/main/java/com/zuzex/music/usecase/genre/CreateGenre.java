package com.zuzex.music.usecase.genre;

import com.zuzex.music.model.Genre;
import reactor.core.publisher.Mono;

public interface CreateGenre {

    Mono<Genre> createGenre(Genre genre);
}
