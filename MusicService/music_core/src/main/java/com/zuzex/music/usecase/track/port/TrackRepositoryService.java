package com.zuzex.music.usecase.track.port;

import com.zuzex.music.model.Artist;
import com.zuzex.music.model.Chart;
import com.zuzex.music.model.Track;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TrackRepositoryService {

    public Flux<Track> getAllTracks();

    public Flux<Track> getByGenre(String genre);

    public Flux<Track> getByArtistId(Long  artist);

    public Mono<Track> saveTrack(Track track);

    public Mono<Track> getByName(String name);

    public Mono<Track> getById(Long id);

    public Flux<Track> saveAll(List<Track> trackList);

    Flux<Track> getByArtistName(String artistName);

    Flux<Track> createChartByListeners();

    Flux<Track> createChartByPlayCounts();

    void updateTracks(List<Track> trackFlux);
}
