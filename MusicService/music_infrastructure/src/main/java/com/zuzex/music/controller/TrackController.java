package com.zuzex.music.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zuzex.music.controller.requests.TrackCreateRequest;
import com.zuzex.music.model.Track;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

public interface TrackController {

    Mono<Track> createTrack(TrackCreateRequest request) throws JsonProcessingException, ExecutionException, InterruptedException;

    Flux<Track> createTracksByAlbum(TrackCreateRequest request) throws JsonProcessingException, ExecutionException, InterruptedException;

    Mono<Track> getTrackById(Long id);

    Mono<Track> getTrackByName(String name);

    Flux<Track> getAllTracks();

    Flux<Track> getTracksByGenre(String genre);

    Flux<Track> getTracksByArtist(String artistName);

}
