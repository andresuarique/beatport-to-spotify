package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.dto.GenreDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface GenreMapper {
    public GenreDTO genreToGenreDTO(Genre genre);
    public Genre genreDTOToGenre(GenreDTO genreDTO);
    public List<GenreDTO> listGenreToListGenreDTO(List<Genre> genres);
    public List<Genre> listGenreDTOToListGenre(List<GenreDTO> genreDTOs);
}
