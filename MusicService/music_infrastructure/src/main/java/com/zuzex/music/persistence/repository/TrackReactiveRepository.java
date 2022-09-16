package com.zuzex.music.persistence.repository;

import com.zuzex.music.persistence.entity.TrackEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TrackReactiveRepository extends ReactiveCrudRepository<TrackEntity, Long> {

    @Query("select t.* from track as t join track_genre as tg on t.id = tg.track_id join genre as g on tg.genre_id = g.id where g.name = :genreName")
    Flux<TrackEntity> findAllByGenresName(String genreName);

    Flux<TrackEntity> findByArtistId(Long artist);

    Mono<TrackEntity> findByName(String name);

    @Query("select t.* from track as t join artist as ar on t.artist_id = ar.id where ar.name =:artistName")
    Flux<TrackEntity> findByArtistName(String artistName);

    @Query("select t.* from track as t order by listeners desc")
    Flux<TrackEntity> findTrackByListeners();

    @Query("select t.* from track as t order by playcount desc")
    Flux<TrackEntity> findTrackByPlayCounts();
}
