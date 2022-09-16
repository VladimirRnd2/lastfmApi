package com.zuzex.music.usecase.artist;

import com.zuzex.music.model.Artist;
import com.zuzex.music.usecase.artist.port.ArtistRepositoryService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class FindArtistImpl implements FindArtist {

    private final ArtistRepositoryService artistRepositoryService;

    @Override
    public Mono<Artist> getById(Long id) {
        return artistRepositoryService.getById(id);
    }

    @Override
    public Mono<Artist> getArtistByName(String artistName) {
        return artistRepositoryService.getByName(artistName);
    }

    @Override
    public Flux<Artist> getAll() {
        return artistRepositoryService.getAll();
    }
}
