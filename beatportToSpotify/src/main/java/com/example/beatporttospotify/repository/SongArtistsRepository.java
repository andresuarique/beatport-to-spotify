package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Artist;
import com.example.beatporttospotify.domain.Song;
import com.example.beatporttospotify.domain.SongArtists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongArtistsRepository extends JpaRepository<SongArtists, Long> {
    public SongArtists findBySongAndArtist(Song song, Artist artist);
}
