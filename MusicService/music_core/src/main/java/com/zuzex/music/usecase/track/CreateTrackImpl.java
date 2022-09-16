package com.zuzex.music.usecase.track;


import com.zuzex.music.model.Track;
import com.zuzex.music.usecase.album.CreateAlbum;
import com.zuzex.music.usecase.artist.CreateArtist;
import com.zuzex.music.usecase.genre.CreateGenre;
import com.zuzex.music.usecase.track.port.TrackRepositoryService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class CreateTrackImpl implements CreateTrack {

    private final TrackRepositoryService trackRepository;

    @Override
    public Mono<Track> createTrack(Track track) {
        return trackRepository.saveTrack(track);
    }

    @Override
    public Flux<Track> createTracksByAlbum(List<Track> trackList) {
        return trackRepository.saveAll(trackList);
    }

}
