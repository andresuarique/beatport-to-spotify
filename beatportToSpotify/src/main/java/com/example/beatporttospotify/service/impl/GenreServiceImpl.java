package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.dto.GenreDTO;
import com.example.beatporttospotify.mapper.GenreMapper;
import com.example.beatporttospotify.repository.GenreRepository;
import com.example.beatporttospotify.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreMapper genreMapper;

    @Override
    public List<GenreDTO> getGenres() {
        return genreMapper.listGenreToListGenreDTO(genreRepository.findAll());
    }

    @Override
    public GenreDTO getGenreById(Long id) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        if(!optionalGenre.isPresent())
            return null;
        return genreMapper.genreToGenreDTO(optionalGenre.get());
    }

    @Override
    public List<GenreDTO> getGenreByName(String name) {
        return genreMapper.listGenreToListGenreDTO(genreRepository.findByName(name));
    }
    @Override
    public List<GenreDTO> getGenreByCode(String code) {
        return genreMapper.listGenreToListGenreDTO(genreRepository.findByCode(code));
    }

    @Override
    public GenreDTO save(GenreDTO genreDTO) {
        try{
            if(!getGenreByCode(genreDTO.getCode()).isEmpty())
                return null;

            Genre genre = genreMapper.genreDTOToGenre(genreDTO);
            genre = genreRepository.save(genre);
            return genreMapper.genreToGenreDTO(genre);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public GenreDTO update(GenreDTO genreDTO) {
        try{
            Optional<Genre> optionalGenre = genreRepository.findById(genreDTO.getId());
            if(!optionalGenre.isPresent())
                return null;
            Genre genre = genreMapper.genreDTOToGenre(genreDTO);
            return genreMapper.genreToGenreDTO(genreRepository.save(genre));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
