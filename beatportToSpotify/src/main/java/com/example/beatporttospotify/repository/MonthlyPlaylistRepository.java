package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.domain.MonthlyPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MonthlyPlaylistRepository extends JpaRepository<MonthlyPlaylist, Long> {
    public MonthlyPlaylist findByGenreAndCreationDate(Genre genre, Date date);
}
