package com.music.shop.musicshop.controller;

import com.mongodb.reactivestreams.client.MongoClient;
import com.music.shop.musicshop.model.MusicInstrument;
import com.music.shop.musicshop.repository.MusicInstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RestController
@CrossOrigin
@RequestMapping("/music")

public class MusicInstrumentController {
    @Autowired
    MusicInstrumentRepository musicInstrumentRepository;

    @GetMapping("/instrument")
    public Flux<MusicInstrument> getAllInstruments() {
        return musicInstrumentRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

//    @GetMapping("/instrument/{page}/{size}")
//    public Flux<MusicInstrument> getAllInstrument(@PathVariable int page, @PathVariable int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return musicInstrumentRepository.findByName("guitar", pageable);
//    }

    @GetMapping("/instrument/{instrumentId}")
    public Mono<MusicInstrument> getInstrumentById(@PathVariable String instrumentId) {
        return musicInstrumentRepository.findById(instrumentId);
    }

    @PostMapping("/instrument")
    public Mono<MusicInstrument> createInstrument(@RequestBody MusicInstrument musicInstrument) {
        System.out.println(musicInstrument);
        return musicInstrumentRepository.save(musicInstrument);
    }

    @PutMapping("/instrument/{instrumentId}")
    public Mono<MusicInstrument> updateInstrument(@PathVariable String instrumentId, @RequestBody MusicInstrument musicInstrument) {
        return musicInstrumentRepository.findById(instrumentId).flatMap(instrument -> {
            instrument.setName(musicInstrument.getName());
            instrument.setPrice(musicInstrument.getPrice());
            return musicInstrumentRepository.save(instrument);
        });
    }

    @DeleteMapping("/instrument/{instrumentId}")
    public Mono<String> deleteInstrument(@PathVariable String instrumentId){
        return musicInstrumentRepository.findById(instrumentId).flatMap(instrument -> musicInstrumentRepository.delete(instrument))
                .then(Mono.just("Deleted"));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadFile(@RequestPart("file") FilePart filePart) throws IOException {
        System.out.println(filePart.filename());
        Path path = Files.createFile(Paths.get("upload", filePart.filename()));
        AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        DataBufferUtils.write(filePart.content(), channel, 0).doOnComplete(() -> {
            System.out.println("Finished");
        }).subscribe();
        System.out.println(path.toString());
        return Mono.just(filePart.filename());
    }

    @GetMapping("/download/{fileName:.+}")
    public Mono<Void> downloadFile(@PathVariable String fileName, ServerHttpResponse response) throws IOException {
        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=upload/" + fileName);
        response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        File file = Paths.get("upload/" + fileName).toFile();
        return zeroCopyResponse.writeWith(file, 0, file.length());
    }
}
