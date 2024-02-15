package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.MonthlyPlaylistDTO;

import java.util.List;

public interface MonthlyPlaylistService {
    public List<MonthlyPlaylistDTO> getMonthlyPlaylists();
    public MonthlyPlaylistDTO getMonthlyPlaylistById(Long id);
    public MonthlyPlaylistDTO getMonthlyPlaylistByGenre(String genreCode);
    public  MonthlyPlaylistDTO save(MonthlyPlaylistDTO monthlyPlaylistDTO);
    public  MonthlyPlaylistDTO update(MonthlyPlaylistDTO monthlyPlaylistDTO);
}
