package com.example.beatporttospotify.dto;


public class ArtistDTO {
    private Long id;
    private String spotifyName;
    private String beatportName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotifyName() {
        return spotifyName;
    }

    public void setSpotifyName(String spotifyName) {
        this.spotifyName = spotifyName;
    }

    public String getBeatportName() {
        return beatportName;
    }

    public void setBeatportName(String beatportName) {
        this.beatportName = beatportName;
    }
}
