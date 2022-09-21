package com.zuzex.music.usecase.track;

import com.zuzex.music.model.Track;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TrackService {

    Mono<Track> createTrack(Track track);

    Flux<Track> createTracksByAlbum(List<Track> trackList);

    Mono<Track> findById(Long id);

    Mono<Track> findByName(String name);

    Flux<Track> findAll();

    Flux<Track> findByGenre(String genre);

    Flux<Track> findByArtistId(Long  artistId);

    Flux<Track> findByArtistName(String artistName);

    void updateTracks(List<Track> trackList);

}
