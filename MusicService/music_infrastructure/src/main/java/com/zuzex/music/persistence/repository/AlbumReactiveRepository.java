package com.zuzex.music.persistence.repository;

import com.zuzex.music.model.Album;
import com.zuzex.music.persistence.entity.AlbumEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AlbumReactiveRepository extends ReactiveCrudRepository<AlbumEntity,Long> {

    @Query("select album.*, artist.name as artist_name, artist.mbid as artist_mbid from album join artist ON artist.id = album.artist_id")
    Flux<AlbumEntity> findAll();

    Mono<AlbumEntity> findByName(String name);
}
