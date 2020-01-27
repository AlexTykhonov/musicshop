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
@NoArgsConstructor
@AllArgsConstructor

public class Lable {
    @Id
    private String id;
    private String name;
    private List<Artist> artists = new ArrayList<>();
    private List<Composer> composers = new ArrayList<>();

    public List<Artist> addArtistToList(Artist artist) {
        this.artists.add(artist);
        return artists;
    }

    public List<Composer> addComposerToList(Composer composer) {
        this.composers.add(composer);
        return composers;
    }
}
