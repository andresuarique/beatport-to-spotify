package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    public List<Genre> findByName(String name);
    public List<Genre> findByUrl(String url);
    public List<Genre> findByCode(String code);
    public List<Genre> findByNameAndCode(String name,String code);
}
