package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.SongArtists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongArtistsRepository extends JpaRepository<SongArtists, Long> {
}
