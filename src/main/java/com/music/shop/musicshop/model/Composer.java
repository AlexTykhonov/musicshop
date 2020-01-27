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

public class Composer {
    @Id
    private String id;
    private String name;
    private String lastName;
    private int age;
   // private List<MusicNote> musicNoteList = new ArrayList<>();

//    public List<MusicNote> addMusicNoteList (MusicNote musicNote) {
//        musicNoteList.add(musicNote);
//        return this.musicNoteList;
//    }
//

}
