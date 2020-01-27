package com.music.shop.musicshop.controller;

import com.music.shop.musicshop.model.Composer;
import com.music.shop.musicshop.repository.ComposerRepository;
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

public class ComposerController {
    @Autowired
    ComposerRepository composerRepository;

    @GetMapping("/compose/all/{page}/{size}")
    public Flux<Composer> findAllComposers(@PathVariable int page, @PathVariable int size) {
        Pageable pageable = PageRequest.of(page, size);
        return composerRepository.findAllBy(pageable);
    }

    @GetMapping("/compose/name/{name}/{page}/{size}")
    public Flux<Composer> findNamePageSort(@PathVariable String name, @PathVariable int page, @PathVariable int size) {
        return composerRepository.findAllByName(name, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "age")));
    }

    @GetMapping("/compose/age/{age}")
    public Flux<Composer> findAge(@PathVariable int age) {
        return composerRepository.ageQuery(age);
    }

    @GetMapping("/compose/{composerId}")
    public Mono<Composer> findComposerById(@PathVariable String composerId) {
        return composerRepository.findById(composerId);
    }

    @PostMapping("/compose")
    public Mono<Composer> createComposer(@RequestBody Composer composer) {
        System.out.println(composer);
        return composerRepository.save(composer);
    }

    @PutMapping("/compose/{composerId}")
    public Mono<Composer> updateComposer(@PathVariable String composerId, @RequestBody Composer composer) {
        return composerRepository.findById(composerId).flatMap(existingComposer -> {
            existingComposer.setName(composer.getName());
            existingComposer.setLastName(composer.getLastName());
            existingComposer.setAge(composer.getAge());
            return composerRepository.save(existingComposer);
        });
    }

    @DeleteMapping("/compose/{composerId}")
    public Mono<String> deleteComposer(@PathVariable String composerId) {
        return composerRepository.findById(composerId).flatMap(composer -> composerRepository.delete(composer)).then(Mono.just("Deleted"));
    }
}
