package com.zuzex.music.persistence.mapper;

import com.zuzex.music.model.Genre;
import com.zuzex.music.persistence.entity.GenreEntity;
import com.zuzex.music.shared.mapper.RepositoryMapper;
import org.springframework.stereotype.Component;

public class GenreMapper implements RepositoryMapper<Genre, GenreEntity> {

    @Override
    public Genre mapToTable(GenreEntity persistenceObject) {
        return Genre.builder()
                .id(persistenceObject.getId())
                .name(persistenceObject.getName())
                .build();
    }

    @Override
    public GenreEntity mapToEntity(Genre tableObject) {
        return GenreEntity.builder()
                .id(tableObject.getId())
                .name(tableObject.getName())
                .build();
    }
}
