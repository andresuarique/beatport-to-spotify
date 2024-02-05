package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    public List<Genre> findByName(String name);
    public Genre findByCode(String code);
}
