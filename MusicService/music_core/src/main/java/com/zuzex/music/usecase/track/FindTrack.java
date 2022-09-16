package com.zuzex.music.usecase.track;

import com.zuzex.music.model.Artist;
import com.zuzex.music.model.Track;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindTrack {

    Mono<Track> findById(Long id);

    Mono<Track> findByName(String name);

    Flux<Track> findAll();

    Flux<Track> findByGenre(String genre);

    Flux<Track> findByArtistId(Long  artistId);

    Flux<Track> findByArtistName(String artistName);
}
