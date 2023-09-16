package com.example.beatporttospotify.model;

import lombok.Data;

import java.util.List;

@Data
public class SpotifySong {
    private String id;
    private String name;
    private List<SpotifyArtist> artists;
}
