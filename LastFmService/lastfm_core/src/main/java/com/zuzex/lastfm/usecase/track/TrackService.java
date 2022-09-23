package com.zuzex.lastfm.usecase.track;

import com.zuzex.lastfm.model.TrackResponse;

import java.util.List;

public interface TrackService {

    TrackResponse createNewTrack(String trackName, String artistName);

    List<TrackResponse> createAlbumTracks(String albumName, String artistName);

    List<TrackResponse> updateTracks(List<TrackResponse> trackResponseList);
}
