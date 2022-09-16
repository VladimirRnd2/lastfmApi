package com.zuzex.music.persistence.repository;

import com.zuzex.music.persistence.entity.TrackGenreEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackGenreReactiveRepository extends ReactiveCrudRepository<TrackGenreEntity,Long > {
}
