package com.example.beatporttospotify.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "spotify_name")
    private String spotifyName;
    @Column(name = "beatport_name")
    private String beatportName;
    @Column(name = "spotify_id")
    private String spotifyId;
    @Column(name = "beatport_image_url")
    private String beatportImageUrl;
    @Column(name = "spotify_image_url")
    private String spotifyImageUrl;
    @Column(name = "status")
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "song")
    private List<SongArtists> songArtists = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "song")
    private List<PlaylistSongs> playlistSongs = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "song")
    private List<MonthlyPlaylistSongs> monthlyPlaylistSongs = new ArrayList<>();

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

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getBeatportImageUrl() {
        return beatportImageUrl;
    }

    public void setBeatportImageUrl(String beatportImageUrl) {
        this.beatportImageUrl = beatportImageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SongArtists> getSongArtists() {
        return songArtists;
    }

    public void setSongArtists(List<SongArtists> songArtists) {
        this.songArtists = songArtists;
    }

    public List<PlaylistSongs> getPlaylistSongs() {
        return playlistSongs;
    }

    public void setPlaylistSongs(List<PlaylistSongs> playlistSongs) {
        this.playlistSongs = playlistSongs;
    }

    public List<MonthlyPlaylistSongs> getMonthlyPlaylistSongs() {
        return monthlyPlaylistSongs;
    }

    public void setMonthlyPlaylistSongs(List<MonthlyPlaylistSongs> monthlyPlaylistSongs) {
        this.monthlyPlaylistSongs = monthlyPlaylistSongs;
    }

    public String getSpotifyImageUrl() {
        return spotifyImageUrl;
    }

    public void setSpotifyImageUrl(String spotifyImageUrl) {
        this.spotifyImageUrl = spotifyImageUrl;
    }
}
