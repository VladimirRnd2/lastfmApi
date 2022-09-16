package com.zuzex.lastfm.usercase.genre;

import com.zuzex.lastfm.model.GenreRs;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CreateGenreImpl implements CreateGenre {

    @Override
    public List<GenreRs> createGenres(Collection<String> genres) {
        return genres.stream().map(genresName -> GenreRs.builder().name(genresName).build()).collect(Collectors.toList());
    }
}
