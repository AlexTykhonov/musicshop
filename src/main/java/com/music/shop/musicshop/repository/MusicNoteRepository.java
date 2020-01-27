package com.music.shop.musicshop.repository;

import com.music.shop.musicshop.model.MusicNote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository

public interface MusicNoteRepository extends ReactiveMongoRepository<MusicNote, String> {
    Flux<MusicNote> findAllBy(Pageable pageable);
    Flux<MusicNote> findAllByAuthor(String author, Pageable pageable);

    @Query("{'pages': ?0}")
    Flux<MusicNote> pagesSearch (int pages);

    @Query("{'pages': { $gt: ?0, $lt: ?1 }}")
    Flux<MusicNote> pagesBetweenSearch(int pagesGT, int pagesLT, Sort author);

    // !!!!!_____!!!!!

    // db.getCollection('musicNote').find({author: "Bob"})
    Flux<MusicNote> findByAuthor(String author);
    @Query("{author: ?0}")
    Flux<MusicNote> findByAuthorQuery(String author);

    // db.getCollection('musicNote').find({author: {$ne: "Bob"}})
    Flux<MusicNote> findByAuthorNot(String author);
    @Query("{$ne: ?0}")
    Flux<MusicNote> findByAuthorNotQuery(String author);

    // db.getCollection('musicNote').find({pages: {$gt: 2, $lt: 10}})
    Flux<MusicNote> findByPagesBetween(int start, int end);
    @Query("{'pages': {$gt: ?0, $lt: ?1}}")
    Flux<MusicNote> findByPagesBetweenQuery(int start, int end);

    // db.getCollection('musicNote').find({$or: [{author: "Lisa"}, {title: "Light"}]})
    Flux<MusicNote> findByAuthorOrTitle(String author, String title);
    @Query("{$or: [{author: ?0}, {title: ?0}]}")
    Flux<MusicNote> findByAuthorOrTitleQuery(String author, String title);

    // db.getCollection('musicNote').find({$and: [{author: "Lisa"}, {title: "Light"}]})
    Flux<MusicNote> findByAuthorAndTitle(String author, String title);
    @Query("{$and: [{author: ?0}, {title: ?0}]}")
    Flux<MusicNote> findByAuthorAndTitleQuery(String author, String title);
}
