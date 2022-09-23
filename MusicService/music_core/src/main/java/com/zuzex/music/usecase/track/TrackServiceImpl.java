package com.zuzex.music.usecase.track;

import com.zuzex.music.model.Track;
import com.zuzex.music.usecase.track.port.TrackStorageService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    private final TrackStorageService trackRepository;

    @Override
    public Mono<Track> createTrack(Track track) {
        return trackRepository.saveTrack(track);
    }

    @Override
    public Flux<Track> createTracksByAlbum(List<Track> trackList) {
        return trackRepository.saveAll(trackList);
    }

    @Override
    public Mono<Track> findById(Long id) {
        return trackRepository.getById(id);
    }

    @Override
    public Mono<Track> findByName(String name) {
        return trackRepository.getByName(name);
    }

    @Override
    public Flux<Track> findAll() {
        return trackRepository.getAllTracks();
    }

    @Override
    public Flux<Track> findByGenre(String genre) {
        return trackRepository.getByGenre(genre);
    }

    @Override
    public Flux<Track> findByArtistId(Long artistId) {
        return trackRepository.getByArtistId(artistId);
    }

    @Override
    public Flux<Track> findByArtistName(String artistName) {
        return trackRepository.getByArtistName(artistName);
    }

    @Override
    public void updateTracks(List<Track> trackList) {
        trackRepository.updateTracks(trackList);
    }
}
