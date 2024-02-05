package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    public Playlist findByGenre(Genre genre);
}
