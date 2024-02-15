package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.domain.MonthlyPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyPlaylistRepository extends JpaRepository<MonthlyPlaylist, Long> {
    public MonthlyPlaylist findByGenre(Genre genre);
}
