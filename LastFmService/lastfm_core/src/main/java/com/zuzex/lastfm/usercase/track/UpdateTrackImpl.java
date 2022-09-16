package com.zuzex.lastfm.usercase.track;

import com.zuzex.lastfm.model.TrackRs;
import de.umass.lastfm.Track;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UpdateTrackImpl implements UpdateTrack {

    private final static String API_KEY = "2fdfc34423cf61d7872486aa750a892f";

    @Override
    public List<TrackRs> updateTracks(List<TrackRs> trackRsList) {
        List<Track> list = trackRsList.stream().map(trackRs -> Track.getInfo(trackRs.getArtist().getName(), trackRs.getName(), API_KEY)).collect(Collectors.toList());
        Flux<Track> trackFlux = Flux.fromIterable(list);
        Flux<TrackRs> trackRsFlux = Flux.fromIterable(trackRsList);
        return Flux.zip(trackRsFlux,trackFlux).map(tuple -> {
            if(tuple.getT1().getListeners() != tuple.getT2().getListeners() || tuple.getT1().getPlaycount() != tuple.getT2().getPlaycount()) {
                tuple.getT1().setListeners(tuple.getT2().getListeners());
                tuple.getT1().setPlaycount(tuple.getT2().getPlaycount());
            }
            return tuple.getT1();
        }).collectList().block();
    }
}
