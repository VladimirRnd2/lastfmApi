package com.zuzex.music.usecase.artist;

import com.zuzex.music.model.Artist;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindArtist {
    Mono<Artist> getById(Long id);

    Mono<Artist> getArtistByName(String artistName);

    Flux<Artist> getAll();
}
