package com.zuzex.music.usecase.genre;

import com.zuzex.music.model.Genre;
import com.zuzex.music.usecase.genre.port.GenreRepositoryService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CreateGenreImpl implements CreateGenre {

    private final GenreRepositoryService genreRepositoryService;

    @Override
    public Mono<Genre> createGenre(Genre genre) {
        return genreRepositoryService.create(genre);
    }
}
