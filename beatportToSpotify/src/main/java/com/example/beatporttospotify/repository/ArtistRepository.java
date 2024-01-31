package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
