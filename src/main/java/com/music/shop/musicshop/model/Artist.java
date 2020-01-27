package com.music.shop.musicshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Artist {
    @Id
    private String id;
    private String name;
    private int age;
    private List<MusicInstrument> musicInstruments = new ArrayList<>();

    public List<MusicInstrument> addListMusicInstrument(List<MusicInstrument> musicInstruments) {
        this.musicInstruments.addAll(musicInstruments);
        return this.musicInstruments;
    }

    public List<MusicInstrument> addMusicInstrumentToList(MusicInstrument musicInstrument) {
        this.musicInstruments.add(musicInstrument);
        return musicInstruments;
    }
}
