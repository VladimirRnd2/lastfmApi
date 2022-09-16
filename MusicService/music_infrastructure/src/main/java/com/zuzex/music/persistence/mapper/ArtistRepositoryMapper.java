package com.zuzex.music.persistence.mapper;

import com.zuzex.music.model.Artist;
import com.zuzex.music.persistence.entity.ArtistEntity;
import com.zuzex.music.shared.mapper.RepositoryMapper;

public class ArtistRepositoryMapper implements RepositoryMapper<Artist, ArtistEntity> {

    @Override
    public Artist mapToTable(ArtistEntity persistenceObject) {
        return Artist.builder()
                .id(persistenceObject.getId())
                .name(persistenceObject.getName())
                .mbid(persistenceObject.getMbid())
                .build();
    }

    @Override
    public ArtistEntity mapToEntity(Artist tableObject) {
        return ArtistEntity.builder()
                .id(tableObject.getId())
                .name(tableObject.getName())
                .mbid(tableObject.getMbid())
                .build();
    }
}
