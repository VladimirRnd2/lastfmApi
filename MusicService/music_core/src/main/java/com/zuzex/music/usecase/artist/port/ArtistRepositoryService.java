package com.zuzex.music.usecase.artist.port;

import com.zuzex.music.model.Artist;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArtistRepositoryService {

    Mono<Artist> createArtist(Artist artist);

    Mono<Artist> getById(Long id);

    Mono<Artist> getByName(String artistName);

    Flux<Artist> getAll();

}
