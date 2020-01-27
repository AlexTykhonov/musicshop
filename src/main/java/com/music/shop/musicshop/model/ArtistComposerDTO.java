package com.music.shop.musicshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ArtistComposerDTO {
    private String composerId;
    private String composerName;
    private String composerLastName;
    private int composerAge;

    private String artistId;
    private String artistName;
    private int artistAge;
    private List<MusicInstrument> musicInstruments = new ArrayList<>();

}
