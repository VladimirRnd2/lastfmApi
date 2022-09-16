package com.zuzex.lastfm.usercase.track;

import com.zuzex.lastfm.model.TrackRs;

import java.util.List;

public interface UpdateTrack {

    List<TrackRs> updateTracks(List<TrackRs> trackRsList);
}
