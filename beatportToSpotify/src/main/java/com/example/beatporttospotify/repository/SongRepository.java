package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByBeatportName(String name);
}
