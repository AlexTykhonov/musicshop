package com.music.shop.musicshop.controller;

import com.music.shop.musicshop.model.Artist;
import com.music.shop.musicshop.model.ArtistComposerDTO;
import com.music.shop.musicshop.model.Composer;
import com.music.shop.musicshop.model.Lable;
import com.music.shop.musicshop.repository.ArtistRepository;
import com.music.shop.musicshop.repository.ComposerRepository;
import com.music.shop.musicshop.repository.LableRepository;
import com.music.shop.musicshop.security.model.User;
import com.music.shop.musicshop.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/music")

public class LableController {
    @Autowired
    LableRepository lableRepository;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    ComposerRepository composerRepository;

    @Autowired
    UserRepository userRepository;

    // Principal это пользователь с точки зрения СпрингСекьюрити

    @GetMapping("/lable")
    public Flux<Lable> findAllLables(Principal principal) {
        Mono <User> user = userRepository.findByUsername(principal.getName());
       // updated
        Flux<Lable> lableList = user.map(u->u.getLableList()).flatMapMany(Flux::fromIterable);
        return lableList;
    }

    @PostMapping("/lable")
    public Mono<Lable> createLable(@RequestBody Lable lable, Principal principal) {

        Mono <User> user = userRepository.findByUsername(principal.getName());
        Mono<Lable> lableMono = lableRepository.save(lable);
        user.zipWith(lableMono).flatMap(tuple-> {
            tuple.getT1().addLableToList(tuple.getT2());
            System.out.println("*********** "+tuple.getT1().getLableList());
            return userRepository.save(tuple.getT1());
        }).subscribe(x-> System.out.println(x));
        return lableMono;
    }

    @PutMapping("/lable/{lableId}")
    public Mono<Lable> aupdateLable(@PathVariable String lableId, @RequestBody Lable lable) {
        return lableRepository.findById(lableId).flatMap(existLable -> {
            existLable.setName(lable.getName());
            existLable.setArtists(lable.getArtists());
            existLable.setComposers(lable.getComposers());
            return lableRepository.save(existLable);
        });
    }

    @PostMapping("/lable/add/new/artist/composer/{lableId}")
    public Mono<Lable> addNewArtistComposerToLable(@PathVariable String lableId, @RequestBody ArtistComposerDTO artistComposerDTO) {
        Mono<Artist> artistMono = artistRepository.save(new Artist(artistComposerDTO.getArtistId(), artistComposerDTO.getArtistName(),
                artistComposerDTO.getArtistAge(), artistComposerDTO.getMusicInstruments()));
        Mono<Composer> composerMono = composerRepository.save(new Composer(artistComposerDTO.getComposerId(), artistComposerDTO.getComposerName(),
                artistComposerDTO.getComposerLastName(), artistComposerDTO.getComposerAge()));
        return lableRepository.findById(lableId).zipWith(artistMono).flatMap(
                tuple -> {
                    tuple.getT1().addArtistToList(tuple.getT2());
                    return lableRepository.save(tuple.getT1());
                }
        ).zipWith(composerMono).flatMap(
                tuple -> {
                    tuple.getT1().addComposerToList(tuple.getT2());
                    return lableRepository.save(tuple.getT1());
                }
        );
    }

    @GetMapping("/lable/add/artist/composer/{lableId}/{artistId}/{composerId}")
    public Mono<Lable> addArtistComposerToLable(@PathVariable String lableId, @PathVariable String artistId, @PathVariable String composerId) {
        Mono<Artist> artistMono = artistRepository.findById(artistId);
        Mono<Composer> composerMono = composerRepository.findById(composerId);
        return lableRepository.findById(lableId).zipWith(artistMono).flatMap(
                tuple -> {
                    tuple.getT1().addArtistToList(tuple.getT2());
                    return lableRepository.save(tuple.getT1());
                }
        ).zipWith(composerMono).flatMap(
                tuple -> {
                    tuple.getT1().addComposerToList(tuple.getT2());
                    return lableRepository.save(tuple.getT1());
                }
        );
    }
    @PostMapping("/lable/add/new/artist/{lableId}")
    public Mono<Lable> addNewArtistToLable(@PathVariable String lableId, @RequestBody Artist artist) {
        Mono<Artist> artistMono = artistRepository.save(artist);
        return lableRepository.findById(lableId).zipWith(artistMono).flatMap(
                tuple -> {
                    tuple.getT1().addArtistToList(tuple.getT2());
                    return lableRepository.save(tuple.getT1());
                }
        );
    }

    @PostMapping("/lable/add/new/composer/{lableId}")
    public Mono<Lable> addNewComposerToLable(@PathVariable String lableId, @RequestBody Composer composer) {
        Mono<Composer> composerMono = composerRepository.save(composer);
        return lableRepository.findById(lableId).zipWith(composerMono).flatMap(
                tuple -> {
                    tuple.getT1().addComposerToList(tuple.getT2());
                    return lableRepository.save(tuple.getT1());
                }
        );
    }

    @GetMapping("/lable/add/artist/{lableId}/{artistId}")
    public Mono<Lable> addArtistToLable(@PathVariable String lableId, @PathVariable String artistId) {
        Mono<Artist> artistMono = artistRepository.findById(artistId);
        return lableRepository.findById(lableId).zipWith(artistMono).flatMap(
                tuple -> {
                    tuple.getT1().addArtistToList(tuple.getT2());
                    return lableRepository.save(tuple.getT1());
                }
        );
    }

    @GetMapping("/lable/add/composer/{lableId}/{composerId}")
    public Mono<Lable> addComposerToLable(@PathVariable String lableId, @PathVariable String composerId) {
        Mono<Composer> composerMono = composerRepository.findById(composerId);
        return lableRepository.findById(lableId).zipWith(composerMono).flatMap(
                tuple -> {
                    tuple.getT1().addComposerToList(tuple.getT2());
                    return lableRepository.save(tuple.getT1());
                }
        );
    }
}
