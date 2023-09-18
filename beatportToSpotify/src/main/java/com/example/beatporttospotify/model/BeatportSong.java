package com.example.beatporttospotify.model;

import lombok.Data;

import java.util.List;

@Data
public class BeatportSong {
    private String name;
    private List<String> artists;
    private String urlImage;
}
