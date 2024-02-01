package com.example.beatporttospotify.model.scraper;


import java.util.List;

public class BeatportToSpotifyRequest {
    private String playlistName;
    private String genre;
    private List<BeatportSong> songs;

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<BeatportSong> getSongs() {
        return songs;
    }

    public void setSongs(List<BeatportSong> songs) {
        this.songs = songs;
    }
}
