package com.music.shop.musicshop.repository;

import com.music.shop.musicshop.model.Lable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface LableRepository extends ReactiveMongoRepository<Lable, String> {
}
