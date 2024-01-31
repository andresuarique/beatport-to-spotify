package com.example.beatporttospotify.model.spotify;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SpotifySong {
    private String id;
    private String name;
    private List<SpotifyArtist> artists;
    private Map<String,String> external_urls;
    private SpotifyAlbum album;
}
