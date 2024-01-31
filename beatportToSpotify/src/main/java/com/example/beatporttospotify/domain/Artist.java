package com.example.beatporttospotify.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artist")
@Data
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "spotify_name")
    private String spotifyName;
    @Column(name = "beatport_name")
    private String beatportName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    private List<SongArtists> songArtists = new ArrayList<>();
}
