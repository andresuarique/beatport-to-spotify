package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Playlist;
import com.example.beatporttospotify.domain.PlaylistSongs;
import com.example.beatporttospotify.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface PlaylistSongsRepository extends JpaRepository<PlaylistSongs, Long> {
    public List<PlaylistSongs> findByPlaylist(Playlist playlist);
    public PlaylistSongs findBySongAndPlaylist(Song song, Playlist playlist);

    @Transactional
    @Modifying
    @Query(value = "UPDATE PlaylistSongs PS SET PS.status = 'DISABLE' WHERE PS.playlist.id = :idPlaylist")
    public void disableAllSongs(@Param("idPlaylist") Long idPlaylist);
}
