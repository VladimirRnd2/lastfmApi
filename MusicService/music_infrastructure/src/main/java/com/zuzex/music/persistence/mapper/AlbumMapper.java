package com.zuzex.music.persistence.mapper;

import com.zuzex.music.model.Album;
import com.zuzex.music.persistence.entity.AlbumEntity;
import com.zuzex.music.shared.mapper.RepositoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public class AlbumMapper implements RepositoryMapper<Album, AlbumEntity> {

    @Override
    public Album mapToTable(AlbumEntity persistenceObject) {
        return Album.builder()
                .id(persistenceObject.getId())
                .name(persistenceObject.getName())
                .mbid(persistenceObject.getMbid())
                .build();
    }

    @Override
    public AlbumEntity mapToEntity(Album tableObject) {
        return AlbumEntity.builder()
                .id(tableObject.getId())
                .name(tableObject.getName())
                .mbid(tableObject.getMbid())
                .build();
    }
}
