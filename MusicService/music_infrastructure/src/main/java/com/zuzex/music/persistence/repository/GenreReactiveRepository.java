package com.zuzex.music.persistence.repository;

import com.zuzex.music.persistence.entity.GenreEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GenreReactiveRepository extends ReactiveCrudRepository<GenreEntity,Long> {

    @Query("select g.* from genre as g join track_genre as tg on g.id = tg.genre_id join track as t on tg.track_id = t.id where t.id = :id")
    Flux<GenreEntity> findAllByTrackId(Long id);

    Mono<GenreEntity> findByName(String name);
}
