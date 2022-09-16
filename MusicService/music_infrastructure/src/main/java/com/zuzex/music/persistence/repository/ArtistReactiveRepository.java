package com.zuzex.music.persistence.repository;

import com.zuzex.music.model.Artist;
import com.zuzex.music.persistence.entity.ArtistEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ArtistReactiveRepository extends ReactiveCrudRepository<ArtistEntity, Long> {

    Mono<ArtistEntity> findByName(String artistName);
}
