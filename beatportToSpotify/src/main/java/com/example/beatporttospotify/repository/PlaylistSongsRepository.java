package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.PlaylistSongs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistSongsRepository extends JpaRepository<PlaylistSongs, Long> {
}
