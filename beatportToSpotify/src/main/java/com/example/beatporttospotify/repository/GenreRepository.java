package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
