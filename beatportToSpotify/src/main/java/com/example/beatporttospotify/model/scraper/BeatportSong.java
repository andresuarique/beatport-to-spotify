package com.example.beatporttospotify.model.scraper;

import lombok.Data;

import java.util.List;

@Data
public class BeatportSong {
    private String name;
    private List<String> artists;
    private String urlImage;
}
