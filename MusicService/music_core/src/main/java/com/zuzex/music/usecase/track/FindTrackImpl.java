package com.zuzex.music.usecase.track;

import com.zuzex.music.model.Artist;
import com.zuzex.music.model.Track;
import com.zuzex.music.usecase.track.port.TrackRepositoryService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class FindTrackImpl implements FindTrack {

    private final TrackRepositoryService trackRepositoryService;

    @Override
    public Mono<Track> findById(Long id) {
        return trackRepositoryService.getById(id);
    }

    @Override
    public Mono<Track> findByName(String name) {
        return trackRepositoryService.getByName(name);
    }

    @Override
    public Flux<Track> findAll() {
        return trackRepositoryService.getAllTracks();
    }

    @Override
    public Flux<Track> findByGenre(String genre) {
        return trackRepositoryService.getByGenre(genre);
    }

    @Override
    public Flux<Track> findByArtistId(Long artistId) {
        return trackRepositoryService.getByArtistId(artistId);
    }

    @Override
    public Flux<Track> findByArtistName(String artistName) {
        return trackRepositoryService.getByArtistName(artistName);
    }
}
