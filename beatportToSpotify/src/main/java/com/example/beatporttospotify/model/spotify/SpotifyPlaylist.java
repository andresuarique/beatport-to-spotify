package com.example.beatporttospotify.model.spotify;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SpotifyPlaylist {
    private String id;
    private String name;
    private Map<String,String> external_urls;
    private List<SpotifyImages> images;
}
