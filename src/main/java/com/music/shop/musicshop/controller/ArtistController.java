package com.music.shop.musicshop.controller;

import com.music.shop.musicshop.model.Artist;
import com.music.shop.musicshop.model.MusicInstrument;
import com.music.shop.musicshop.repository.ArtistRepository;
import com.music.shop.musicshop.repository.MusicInstrumentRepository;
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

public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    MusicInstrumentRepository musicInstrumentRepository;

    @GetMapping("/artist/sort")
    public Flux<Artist> findAllArtists() {
        return artistRepository.findAll(Sort.by(Sort.Direction.ASC, "age"));
    }

    @GetMapping("/artist/sort/{name}/{page}/{size}")
    public Flux<Artist> findPageSort(@PathVariable String name, @PathVariable int page, @PathVariable int size) {
        return artistRepository.findAllByName(name, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "age")));
    }

    @GetMapping("/artist/find/{name}")
    public Flux<Artist> findByName (@PathVariable String name) {
        return artistRepository.findByName(name);
    }

    @GetMapping("/artist/findage/{age}")
    public Flux<Artist> findByAge (@PathVariable int age) {
        return artistRepository.ageQuery(age);
    }

    @GetMapping("/artist/{artistId}")
    public Mono<Artist> findArtistById(@PathVariable String artistId) {
        return artistRepository.findById(artistId);
    }

    @PostMapping("/artist")
    public Mono<Artist> createArtist(@RequestBody Artist artist) {
        System.out.println(artist);
        return artistRepository.save(artist);
    }

    @PutMapping("/artist/{artistId}")
    public Mono<Artist> updateArtist(@PathVariable String artistId, @RequestBody Artist artist) {
        return artistRepository.findById(artistId).flatMap(existingArtist -> {
            existingArtist.setName(artist.getName());
            existingArtist.setAge(artist.getAge());
            return artistRepository.save(existingArtist);
        });
    }

    @DeleteMapping("/artist/{artistId}")
    public Mono<String> deleteArtist(@PathVariable String artistId) {
        return artistRepository.findById(artistId).flatMap(artist -> artistRepository.delete(artist)).then(Mono.just("Deleted"));
    }

//    @PostMapping("/artist/add/instrument/{artistId}")
//    Mono<Artist> addInstrumentToArtist(@PathVariable String artistId, @RequestBody MusicInstrument musicInstrument) {
//        Mono<MusicInstrument> musicInstrumentMono = musicInstrumentRepository.save(musicInstrument);
//        return artistRepository.findById(artistId).zipWith(musicInstrumentMono).flatMap(tuple -> {
//            tuple.getT1().setMusicInstrument(tuple.getT2());
//            return artistRepository.save(tuple.getT1());
//        });
//    }
//
//    @PostMapping("/artist/bind/instrument/{artistId}/{instrumentId}")
//    Mono<Artist> bindInstrumentToArtist(@PathVariable String artistId, @PathVariable String instrumentId) {
//        return artistRepository.findById(artistId).zipWith(musicInstrumentRepository.findById(instrumentId)).flatMap(tuple -> {
//            tuple.getT1().setMusicInstrument(tuple.getT2());
//            return artistRepository.save(tuple.getT1());
//        });
//    }

    @PostMapping("/artist/add/instrument/{artistId}")
    public Mono<Artist> addMusicInstrumentToList(@PathVariable String artistId, @RequestBody MusicInstrument musicInstrument) {
        Mono<MusicInstrument> musicInstrumentMono = musicInstrumentRepository.save(musicInstrument);
        return artistRepository.findById(artistId).zipWith(musicInstrumentMono).flatMap(
                tuple -> {
                    tuple.getT1().addMusicInstrumentToList(tuple.getT2());
                    return artistRepository.save(tuple.getT1());
                }
        );
    }

    @GetMapping("/artist/add/instrument/{artistId}/{instrumentId}")
    public Mono<Artist> addInstrumentToArtist(@PathVariable String artistId, @PathVariable String instrumentId) {
        return artistRepository.findById(artistId).zipWith(musicInstrumentRepository.findById(instrumentId)).flatMap(
                tuple -> {
                    tuple.getT1().addMusicInstrumentToList(tuple.getT2());
                    return artistRepository.save(tuple.getT1());
                }
        );
    }
}
