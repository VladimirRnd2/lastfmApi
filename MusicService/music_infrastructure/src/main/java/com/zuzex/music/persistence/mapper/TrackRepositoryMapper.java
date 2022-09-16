package com.zuzex.music.persistence.mapper;

import com.zuzex.music.model.Track;
import com.zuzex.music.persistence.entity.TrackEntity;
import com.zuzex.music.shared.mapper.RepositoryMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrackRepositoryMapper implements RepositoryMapper<Track, TrackEntity> {
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
}
