package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.SongDTO;

import java.util.List;

public interface SongService {
    public List<SongDTO> getSongs();
    public List<SongDTO> getSongeByName(String name);
    public SongDTO getSongsById(Long id);
    public SongDTO save(SongDTO songDTO);
    public SongDTO update(SongDTO songDTO);
}
