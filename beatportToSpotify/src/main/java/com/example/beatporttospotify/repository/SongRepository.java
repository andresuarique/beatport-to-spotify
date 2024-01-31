package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
