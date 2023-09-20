package com.example.beatporttospotify.model;

import lombok.Data;

import java.util.List;

@Data
public class BeatportToSpotifyRequest {
    private String playlistName;
    private String genre;
    private List<BeatportSong> songs;
}
