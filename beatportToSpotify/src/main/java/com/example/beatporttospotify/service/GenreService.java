package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.GenreDTO;

import java.util.List;

public interface GenreService {
    public List<GenreDTO> getGenres();
    public GenreDTO getGenreById(Long id);
    public List<GenreDTO> getGenreByName(String name);
    public List<GenreDTO> getGenreByUrl(String url);
    public List<GenreDTO> getGenreByCode(String code);
    public List<GenreDTO> getGenreByNameAndCode(String name, String code);
    public GenreDTO save(GenreDTO genreDTO);
    public GenreDTO update(GenreDTO genreDTO);
}
