package com.example.beatporttospotify.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "spotify_name")
    private String spotifyName;
    @Column(name = "beatport_name")
    private String beatportName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    private List<SongArtists> songArtists = new ArrayList<>();

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

    public List<SongArtists> getSongArtists() {
        return songArtists;
    }

    public void setSongArtists(List<SongArtists> songArtists) {
        this.songArtists = songArtists;
    }
}
