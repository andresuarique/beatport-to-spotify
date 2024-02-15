package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.MonthlyPlaylistDTO;
import com.example.beatporttospotify.dto.MonthlyPlaylistSongsDTO;
import com.example.beatporttospotify.dto.PlaylistDTO;
import com.example.beatporttospotify.dto.PlaylistSongsDTO;

import java.util.List;

public interface MonthlyPlaylistSongsService {
    public List<MonthlyPlaylistSongsDTO> getMonthlyPlaylistSongs();
    public MonthlyPlaylistSongsDTO getMonthlyPlaylistSongsById(Long id);
    public List<MonthlyPlaylistSongsDTO> getMonthlyPlaylistSongsByMonthlyPlaylist(MonthlyPlaylistSongsDTO monthlyPlaylistSongsDTO);
    public MonthlyPlaylistSongsDTO save(MonthlyPlaylistSongsDTO monthlyPlaylistDTO);
    public MonthlyPlaylistSongsDTO update(MonthlyPlaylistSongsDTO monthlyPlaylistDTO);
    public void disableAllSongs(Long idMonthlyPlaylist);
}
