package com.zuzex.lastfm.usecase.genre;

import com.zuzex.lastfm.model.GenreResponse;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    @Override
    public List<GenreResponse> createGenres(Collection<String> genres) {
        return genres
                .stream()
                .map(genresName -> GenreResponse.builder()
                        .name(genresName)
                        .build())
                .collect(Collectors.toList());
    }
}
