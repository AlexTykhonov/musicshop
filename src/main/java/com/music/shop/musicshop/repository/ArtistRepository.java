package com.music.shop.musicshop.repository;

import com.music.shop.musicshop.model.Artist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository

public interface ArtistRepository extends ReactiveMongoRepository<Artist, String> {
    Flux<Artist> findAllByName (String name, Pageable pageable);
    Flux<Artist> findByName (String name);

    @Query("{'age' : ?0}")
    Flux<Artist> ageQuery (int age);
}
