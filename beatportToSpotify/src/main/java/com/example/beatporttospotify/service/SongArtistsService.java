package com.example.beatporttospotify.service;


import com.example.beatporttospotify.dto.SongArtistsDTO;

import java.util.List;

public interface SongArtistsService {
    public List<SongArtistsDTO> getSongsArtists();
    public SongArtistsDTO getSongsArtistsById(Long id);
    public SongArtistsDTO save(SongArtistsDTO songArtistsDTO);
    public SongArtistsDTO update(SongArtistsDTO songArtistsDTO);
}
