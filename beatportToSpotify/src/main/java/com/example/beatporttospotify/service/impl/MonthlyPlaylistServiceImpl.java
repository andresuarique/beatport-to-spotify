package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.domain.MonthlyPlaylist;
import com.example.beatporttospotify.dto.MonthlyPlaylistDTO;
import com.example.beatporttospotify.mapper.GenreMapper;
import com.example.beatporttospotify.mapper.MonthlyPlaylistMapper;
import com.example.beatporttospotify.repository.MonthlyPlaylistRepository;
import com.example.beatporttospotify.service.GenreService;
import com.example.beatporttospotify.service.MonthlyPlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MonthlyPlaylistServiceImpl implements MonthlyPlaylistService {
    @Autowired
    private MonthlyPlaylistRepository monthlyPlaylistRepository;
    @Autowired
    private MonthlyPlaylistMapper monthlyPlaylistMapper;
    @Autowired
    private GenreService genreService;
    @Autowired
    private GenreMapper genreMapper;
    private static final Logger logger = LoggerFactory.getLogger(PlaylistServiceImpl.class);
    @Override
    public List<MonthlyPlaylistDTO> getMonthlyPlaylists() {
        return monthlyPlaylistMapper.listMonthlyPlaylistToListMonthlyPlaylistDTO(monthlyPlaylistRepository.findAll());
    }

    @Override
    public MonthlyPlaylistDTO getMonthlyPlaylistById(Long id) {
        Optional<MonthlyPlaylist> optionalMonthlyPlaylist = monthlyPlaylistRepository.findById(id);
        return optionalMonthlyPlaylist.map(monthlyPlaylist -> monthlyPlaylistMapper.monthlyPlaylistToMonthlyPlaylistDTO(monthlyPlaylist)).orElse(null);
    }

    @Override
    public MonthlyPlaylistDTO getMonthlyPlaylistByGenreAndMonth(int year,int month, String genreCode){
        Genre genre = genreMapper.genreDTOToGenre(genreService.getGenreByCode(genreCode).get(0));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, 1);
        Date date = calendar.getTime();
        MonthlyPlaylist monthlyPlaylist = monthlyPlaylistRepository.findByGenreAndCreationDate(genre,date);
        return monthlyPlaylistMapper.monthlyPlaylistToMonthlyPlaylistDTO(monthlyPlaylist);
    }

    @Override
    public MonthlyPlaylistDTO save(MonthlyPlaylistDTO monthlyPlaylistDTO) {
        try{
            MonthlyPlaylist monthlyPlaylist = monthlyPlaylistMapper.monthlyPlaylistDTOToMonthlyPlaylist(monthlyPlaylistDTO);
            monthlyPlaylist = monthlyPlaylistRepository.save(monthlyPlaylist);
            return monthlyPlaylistMapper.monthlyPlaylistToMonthlyPlaylistDTO(monthlyPlaylist);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public MonthlyPlaylistDTO update(MonthlyPlaylistDTO monthlyPlaylistDTO) {
        try{
            Optional<MonthlyPlaylist> optionalMonthlyPlaylist = monthlyPlaylistRepository.findById(monthlyPlaylistDTO.getId());
            if(!optionalMonthlyPlaylist.isPresent()){
                return null;
            }
            MonthlyPlaylist monthlyPlaylist = monthlyPlaylistMapper.monthlyPlaylistDTOToMonthlyPlaylist(monthlyPlaylistDTO);
            return monthlyPlaylistMapper.monthlyPlaylistToMonthlyPlaylistDTO(monthlyPlaylistRepository.save(monthlyPlaylist));
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }
}
