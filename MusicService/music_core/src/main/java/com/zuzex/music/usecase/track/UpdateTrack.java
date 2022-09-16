package com.zuzex.music.usecase.track;

import com.zuzex.music.model.Track;
import reactor.core.publisher.Flux;

import java.util.List;

public interface UpdateTrack {

    void updateTracks(List<Track> trackFlux);
}
