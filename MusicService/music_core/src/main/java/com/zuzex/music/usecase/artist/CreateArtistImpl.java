package com.zuzex.music.usecase.artist;

import com.zuzex.music.model.Artist;
import com.zuzex.music.usecase.artist.port.ArtistRepositoryService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CreateArtistImpl implements CreateArtist {

    private final ArtistRepositoryService artistRepositoryService;

    @Override
    public Mono<Artist> createArtist(Artist artist) {
        return artistRepositoryService.createArtist(artist);
    }
}
