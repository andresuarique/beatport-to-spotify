package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.ArtistDTO;

import java.util.List;

public interface ArtistService {
    public List<ArtistDTO> getArtists();
    public List<ArtistDTO> getArtistByNames(String name);
    public ArtistDTO getArtistsById(Long id);
    public ArtistDTO save(ArtistDTO artistDTO);
    public ArtistDTO update(ArtistDTO artistDTO);
}
