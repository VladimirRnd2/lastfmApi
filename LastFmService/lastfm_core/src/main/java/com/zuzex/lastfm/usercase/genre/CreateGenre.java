package com.zuzex.lastfm.usercase.genre;

import com.zuzex.lastfm.model.GenreRs;

import java.util.Collection;
import java.util.List;

public interface CreateGenre {

    List<GenreRs> createGenres(Collection<String> genres);
}
