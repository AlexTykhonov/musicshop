package com.music.shop.musicshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MusicNote {
    @Id
    private String id;
    private String title;
    private String author;
    private int pages;
    private Composer composer;
}
