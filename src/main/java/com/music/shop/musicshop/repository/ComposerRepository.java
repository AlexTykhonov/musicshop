package com.music.shop.musicshop.repository;

import com.music.shop.musicshop.model.Composer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository

public interface ComposerRepository extends ReactiveMongoRepository<Composer, String> {
    Flux<Composer> findAllBy(Pageable pageable);
    Flux<Composer> findAllByName(String name, Pageable pageable);

// db.getCollection('composer').find({'age' : 21})
    @Query("{'age' : ?0}")
    Flux<Composer> ageQuery (int age);
}
