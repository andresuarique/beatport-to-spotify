package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByBeatportName(String name);
}
