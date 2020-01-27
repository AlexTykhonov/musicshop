package com.music.shop.musicshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor

public class MusicInstrument {
    @Id
    private String id;
    private String name;
    private double price;

}
