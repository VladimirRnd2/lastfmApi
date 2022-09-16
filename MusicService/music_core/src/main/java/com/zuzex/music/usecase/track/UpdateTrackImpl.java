package com.zuzex.music.usecase.track;

import com.zuzex.music.model.Track;
import com.zuzex.music.usecase.track.port.TrackRepositoryService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
public class UpdateTrackImpl implements UpdateTrack {

    private final TrackRepositoryService trackRepositoryService;

    @Override
    public void updateTracks(List<Track> trackFlux) {
        trackRepositoryService.updateTracks(trackFlux);
    }
}
