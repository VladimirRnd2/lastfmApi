package com.zuzex.music.usecase.track;

import com.zuzex.music.model.Track;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CreateTrack {

    Mono<Track> createTrack(Track Track);

    Flux<Track> createTracksByAlbum(List<Track> trackList);

}
