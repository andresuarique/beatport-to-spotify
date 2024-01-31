package com.example.beatporttospotify.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "song_artists")
@Data
public class SongArtists {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;
}
