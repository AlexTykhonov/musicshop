package com.music.shop.musicshop.controller;

import com.music.shop.musicshop.model.Composer;
import com.music.shop.musicshop.model.MusicNote;
import com.music.shop.musicshop.repository.ComposerRepository;
import com.music.shop.musicshop.repository.MusicNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
@RequestMapping("/music")

public class MusicNoteController {
    @Autowired
    MusicNoteRepository musicNoteRepository;
    @Autowired
    ComposerRepository composerRepository;

    @PostMapping("/note")
    public Mono<MusicNote> createNote(@RequestBody MusicNote musicNote) {
        System.out.println(musicNote);
        return musicNoteRepository.save(musicNote);
    }

    @PutMapping("/note/{noteId}")
    public Mono<MusicNote> updateNote(@PathVariable String noteId, @RequestBody MusicNote musicNote) {
        return musicNoteRepository.findById(noteId).flatMap(existingNote -> {
            existingNote.setTitle(musicNote.getTitle());
            existingNote.setAuthor(musicNote.getAuthor());
            existingNote.setPages(musicNote.getPages());
            return musicNoteRepository.save(existingNote);
        });
    }

    @DeleteMapping("/note/{noteId}")
    public Mono<String> deleteNote(@PathVariable String noteId) {
        return musicNoteRepository.findById(noteId).flatMap(note -> musicNoteRepository.delete(note)).then(Mono.just("Deleted"));
    }

    @GetMapping("/note/all/{page}/{size}")
    public Flux<MusicNote> findAllNotes(@PathVariable int page, @PathVariable int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "author"));
        return musicNoteRepository.findAllBy(pageable);
    }

    @GetMapping("/note/{author}/{page}/{size}")
    public Flux<MusicNote> findAllAuthors(@PathVariable String author, @PathVariable int page, @PathVariable int size) {
        return musicNoteRepository.findAllByAuthor(author, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "title")));
    }

    @GetMapping("/note/{noteId}")
    public Mono<MusicNote> findNoteById(@PathVariable String noteId) {
        return musicNoteRepository.findById(noteId);
    }

    @GetMapping("/note/search/{page}")
    public Flux<MusicNote> findNoteByPages(@PathVariable int page) {
        return musicNoteRepository.pagesSearch(page);
    }

    @GetMapping("/note/find/{pagesGT}/{pagesLT}")
    public Flux<MusicNote> findNoteByAuthorAndPages(@PathVariable int pagesGT, @PathVariable int pagesLT) {
        return musicNoteRepository.pagesBetweenSearch(pagesGT, pagesLT, Sort.by(Sort.Direction.ASC, "author"));
    }

    @PostMapping("/note/add/composer/{id}")
    public Mono<MusicNote> addComposerToNote(@PathVariable String id, @RequestBody Composer composer) {
            Mono<Composer> composerMono = composerRepository.save(composer);
            return musicNoteRepository.findById(id).zipWith(composerMono).flatMap(
                    tuple -> {
                        tuple.getT1().setComposer(tuple.getT2());
                        return musicNoteRepository.save(tuple.getT1());
                    }
            );
    }

    @PostMapping("/note/change/composer/{musicNoteId}/{composerId}")
    public Mono<MusicNote> addComposerToNote(@PathVariable String musicNoteId, @PathVariable String composerId) {
        return musicNoteRepository.findById(musicNoteId).zipWith(composerRepository.findById(composerId)).flatMap(
                tuple -> {
                    tuple.getT1().setComposer(tuple.getT2());
                    return musicNoteRepository.save(tuple.getT1());
                }
        );
    }
}
