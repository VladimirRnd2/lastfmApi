package com.zuzex.music.persistence.mapper;

import com.zuzex.music.model.Album;
import com.zuzex.music.model.Artist;
import com.zuzex.music.model.Genre;
import com.zuzex.music.model.Track;
import com.zuzex.music.persistence.entity.TrackEntity;
import com.zuzex.music.shared.mapper.RepositoryMapper;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public class TrackMapper implements RepositoryMapper<Track, TrackEntity> {

    @Override
    public Track mapToTable(TrackEntity persistenceObject) {
        return Track.builder()
                .id(persistenceObject.getId())
                .name(persistenceObject.getName())
                .mbid(persistenceObject.getMbid())
                .listeners(persistenceObject.getListeners())
                .playcount(persistenceObject.getPlaycount())
                .build();
    }

    @Override
    public TrackEntity mapToEntity(Track tableObject) {
        return TrackEntity.builder()
                .id(tableObject.getId())
                .name(tableObject.getName())
                .mbid(tableObject.getMbid())
                .artistId(tableObject.getArtist().getId())
                .albumId(tableObject.getAlbum().getId())
                .listeners(tableObject.getListeners())
                .playcount(tableObject.getPlaycount())
                .build();
    }

    public Mono<Track> mapToTrackFromTrackEntity(TrackEntity trackEntity,Mono<Artist> artistMono, Mono<Album> albumMono, Mono<List<Genre>> listGenreMono) {
        return Mono.zip(artistMono, albumMono, listGenreMono).map(tuple -> Track.builder()
                .id(trackEntity.getId())
                .name(trackEntity.getName())
                .mbid(trackEntity.getMbid())
                .playcount(trackEntity.getPlaycount())
                .listeners(trackEntity.getListeners())
                .artist(Artist.builder()
                        .id(trackEntity.getArtistId())
                        .name(tuple.getT1().getName())
                        .mbid(tuple.getT1().getMbid())
                        .build())
                .album(Album.builder()
                        .id(trackEntity.getAlbumId())
                        .name(tuple.getT2().getName())
                        .mbid(tuple.getT2().getMbid())
                        .build())
                .genres(tuple.getT3().stream().map(genreEntity -> Genre.builder().id(genreEntity.getId()).name(genreEntity.getName()).build()).collect(Collectors.toList()))
                .build());
    }

    public TrackEntity mapToTrackEntityWithSave(Track track, Album albumEntity, Long artistId) {
        return TrackEntity.builder()
                .name(track.getName())
                .mbid(track.getMbid())
                .listeners(track.getListeners())
                .playcount(track.getPlaycount())
                .albumId(albumEntity.getId())
                .artistId(artistId)
                .build();
    }
}
