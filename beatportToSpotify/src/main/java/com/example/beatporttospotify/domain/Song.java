package com.example.beatporttospotify.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "song")
@Data
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column(name = "status")
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "song")
    private List<SongArtists> songArtists = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "song")
    private List<PlaylistSongs> playlistSongs = new ArrayList<>();
}
