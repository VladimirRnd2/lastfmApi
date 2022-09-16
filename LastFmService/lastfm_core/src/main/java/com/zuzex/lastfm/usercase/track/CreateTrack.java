package com.zuzex.lastfm.usercase.track;

import com.zuzex.lastfm.model.TrackRs;

import java.util.List;

public interface CreateTrack {

    TrackRs createNewTrack(String trackName, String artistName);

    List<TrackRs> createAlbumTracks(String albumName, String artistName);
}
