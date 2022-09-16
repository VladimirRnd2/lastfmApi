package com.zuzex.lastfm.usercase.track;

import com.zuzex.lastfm.model.AlbumRs;
import com.zuzex.lastfm.model.ArtistRs;
import com.zuzex.lastfm.model.GenreRs;
import com.zuzex.lastfm.model.TrackRs;
import com.zuzex.lastfm.usercase.album.CreateAlbum;
import com.zuzex.lastfm.usercase.artist.CreateArtist;
import com.zuzex.lastfm.usercase.genre.CreateGenre;
import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Track;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CreateTrackImpl implements CreateTrack {

    private final static String API_KEY = "2fdfc34423cf61d7872486aa750a892f";

    private final CreateAlbum createAlbum;
    private final CreateArtist createArtist;
    private final CreateGenre createGenre;

    @Override
    public TrackRs createNewTrack(String trackName, String artistName) {
        Track info = Track.getInfo(artistName, trackName, API_KEY);
        ArtistRs artist = createArtist.createArtist(info.getArtist(), info.getArtistMbid());
        AlbumRs album = createAlbum.createAlbum(info.getAlbum(), info.getAlbumMbid(), artist);
        List<GenreRs> genreRsList = createGenre.createGenres(info.getTags());
        return TrackRs.builder()
                .name(info.getName())
                .mbid(info.getMbid())
                .listeners(info.getListeners())
                .playcount(info.getPlaycount())
                .album(album)
                .artist(artist)
                .genres(genreRsList)
                .build();
    }

    @Override
    public List<TrackRs> createAlbumTracks(String albumName, String artistName) {
        Album info = Album.getInfo(artistName, albumName, API_KEY);
        return info.getTracks().stream().map(track -> createNewTrack(track.getName(),artistName)).collect(Collectors.toList());
    }
}
