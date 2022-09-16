package com.zuzex.music.usecase.artist;

import com.zuzex.music.model.Artist;
import reactor.core.publisher.Mono;

public interface CreateArtist {

    Mono<Artist> createArtist(Artist artist);
}
