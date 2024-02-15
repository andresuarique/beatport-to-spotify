package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface MonthlyPlaylistSongsRepository extends JpaRepository<MonthlyPlaylistSongs, Long> {
    public List<MonthlyPlaylistSongs> findByMonthlyPlaylist(MonthlyPlaylist playlist);
    public MonthlyPlaylistSongs findBySongAndMonthlyPlaylist(Song song, MonthlyPlaylist monthlyPlaylist);

    @Transactional
    @Modifying
    @Query(value = "UPDATE MonthlyPlaylistSongs MPS SET MPS.status = 'DISABLE' WHERE MPS.monthlyPlaylist.id = :idPlaylist")
    public void disableAllSongs(@Param("idPlaylist") Long idPlaylist);
}
