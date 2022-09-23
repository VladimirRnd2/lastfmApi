package com.zuzex.lastfm.usecase.track;

import com.zuzex.lastfm.model.AlbumResponse;
import com.zuzex.lastfm.model.ArtistResponse;
import com.zuzex.lastfm.model.GenreResponse;
import com.zuzex.lastfm.model.TrackResponse;
import com.zuzex.lastfm.usecase.album.AlbumService;
import com.zuzex.lastfm.usecase.artist.ArtistService;
import com.zuzex.lastfm.usecase.genre.GenreService;
import de.umass.lastfm.Album;
import de.umass.lastfm.Track;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

import static com.zuzex.lastfm.usecase.track.consts.MainConsts.API_KEY;

@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    private final AlbumService albumService;
    private final ArtistService artistService;
    private final GenreService genreService;

    @Override
    public TrackResponse createNewTrack(String trackName, String artistName) {
        Track info = Track.getInfo(artistName, trackName, API_KEY);
        ArtistResponse artist = artistService.createArtist(info.getArtist(), info.getArtistMbid());
        AlbumResponse album = albumService.createAlbum(info.getAlbum(), info.getAlbumMbid(), artist);
        List<GenreResponse> genreResponseList = genreService.createGenres(info.getTags());
        return TrackResponse.builder()
                .name(info.getName())
                .mbid(info.getMbid())
                .listeners(info.getListeners())
                .playcount(info.getPlaycount())
                .album(album)
                .artist(artist)
                .genres(genreResponseList)
                .build();
    }

    @Override
    public List<TrackResponse> createAlbumTracks(String albumName, String artistName) {
        Album info = Album.getInfo(artistName, albumName, API_KEY);
        return info
                .getTracks()
                .stream()
                .map(track -> createNewTrack(track.getName(), artistName))
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackResponse> updateTracks(List<TrackResponse> trackResponseList) {
        return trackResponseList.stream()
                .map(trackResponse ->
                        updateTrack(Track.getInfo
                                        (trackResponse.getArtist().getName(), trackResponse.getName(), API_KEY)
                                , trackResponse)
                ).collect(Collectors.toList());
    }

    private TrackResponse updateTrack(Track trackInfo, TrackResponse trackResponse) {
        trackResponse.setListeners(trackInfo.getListeners());
        trackResponse.setPlaycount(trackInfo.getPlaycount());
        return trackResponse;
    }
}
