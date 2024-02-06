package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Playlist;
import com.example.beatporttospotify.domain.PlaylistSongs;
import com.example.beatporttospotify.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistSongsRepository extends JpaRepository<PlaylistSongs, Long> {
    public List<PlaylistSongs> findByPlaylist(Playlist playlist);
    public PlaylistSongs findBySongAndPlaylist(Song song, Playlist playlist);
}
