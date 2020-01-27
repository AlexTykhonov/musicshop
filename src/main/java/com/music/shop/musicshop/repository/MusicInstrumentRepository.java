package com.music.shop.musicshop.repository;

import com.music.shop.musicshop.model.MusicInstrument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository

public interface MusicInstrumentRepository extends ReactiveMongoRepository<MusicInstrument, String> {
//    Flux<MusicInstrument> findByName(String name, Pageable page);
}
