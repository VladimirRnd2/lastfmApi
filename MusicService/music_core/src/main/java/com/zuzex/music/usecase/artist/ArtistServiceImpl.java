package com.zuzex.music.usecase.artist;

import com.zuzex.music.model.Artist;
import com.zuzex.music.usecase.artist.port.ArtistStorageService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistStorageService artistStorageService;

    @Override
    public Mono<Artist> createArtist(Artist artist) {
        return artistStorageService.createArtist(artist);
    }

    @Override
    public Mono<Artist> getById(Long id) {
        return artistStorageService.getById(id);
    }

    @Override
    public Mono<Artist> getArtistByName(String artistName) {
        return artistStorageService.getByName(artistName);
    }

    @Override
    public Flux<Artist> getAll() {
        return artistStorageService.getAll();
    }
}
