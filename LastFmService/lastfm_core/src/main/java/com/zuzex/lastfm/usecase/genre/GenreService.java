package com.zuzex.lastfm.usecase.genre;

import com.zuzex.lastfm.model.GenreResponse;

import java.util.Collection;
import java.util.List;

public interface GenreService {

    List<GenreResponse> createGenres(Collection<String> genres);
}
