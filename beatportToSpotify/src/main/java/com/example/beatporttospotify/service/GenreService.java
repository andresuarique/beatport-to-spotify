package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.GenreDTO;

import java.util.List;

public interface GenreService {
    public List<GenreDTO> getGenres();
    public GenreDTO getGenreById(Long id);
    public List<GenreDTO> getGenreByName(String name);
    public GenreDTO getGenreByCode(String code);
    public GenreDTO save(GenreDTO genreDTO);
    public GenreDTO update(GenreDTO genreDTO);
}