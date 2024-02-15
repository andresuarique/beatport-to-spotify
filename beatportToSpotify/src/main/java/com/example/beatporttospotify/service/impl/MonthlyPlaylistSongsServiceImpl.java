package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.*;
import com.example.beatporttospotify.dto.MonthlyPlaylistDTO;
import com.example.beatporttospotify.dto.MonthlyPlaylistSongsDTO;
import com.example.beatporttospotify.mapper.MonthlyPlaylistMapper;
import com.example.beatporttospotify.mapper.MonthlyPlaylistSongsMapper;
import com.example.beatporttospotify.mapper.PlaylistMapper;
import com.example.beatporttospotify.mapper.PlaylistSongsMapper;
import com.example.beatporttospotify.repository.*;
import com.example.beatporttospotify.service.MonthlyPlaylistService;
import com.example.beatporttospotify.service.MonthlyPlaylistSongsService;
import com.example.beatporttospotify.service.PlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonthlyPlaylistSongsServiceImpl implements MonthlyPlaylistSongsService {
    @Autowired
    private MonthlyPlaylistSongsRepository monthlyPlaylistSongsRepository;
    @Autowired
    private MonthlyPlaylistSongsMapper monthlyPlaylistSongsMapper;
    @Autowired
    private MonthlyPlaylistService monthlyPlaylistService;
    @Autowired
    private MonthlyPlaylistMapper monthlyPlaylistMapper;
    @Autowired
    private MonthlyPlaylistRepository monthlyPlaylistRepository;
    @Autowired
    private SongRepository songRepository;
    private static final Logger logger = LoggerFactory.getLogger(PlaylistSongsServiceImpl.class);

    @Override
    public List<MonthlyPlaylistSongsDTO> getMonthlyPlaylistSongs() {
        return monthlyPlaylistSongsMapper.listMonthlyPlaylistSongsToListMonthlyPlaylistSongsDTO(monthlyPlaylistSongsRepository.findAll());
    }

    @Override
    public MonthlyPlaylistSongsDTO getMonthlyPlaylistSongsById(Long id) {
        Optional<MonthlyPlaylistSongs> optionalMonthlyPlaylistSongs = monthlyPlaylistSongsRepository.findById(id);
        return optionalMonthlyPlaylistSongs.map(monthlyPlaylistSongs -> monthlyPlaylistSongsMapper.monthlyPlaylistSongsToMonthlyPlaylistSongsDTO(monthlyPlaylistSongs)).orElse(null);

    }

    @Override
    public List<MonthlyPlaylistSongsDTO> getMonthlyPlaylistSongsByMonthlyPlaylist(MonthlyPlaylistSongsDTO monthlyPlaylistSongsDTO) {
        MonthlyPlaylist monthlyPlaylist = monthlyPlaylistMapper.monthlyPlaylistDTOToMonthlyPlaylist(monthlyPlaylistService.getMonthlyPlaylistById(monthlyPlaylistSongsDTO.getId()));
        if(monthlyPlaylist == null){
            return null;
        }
        return monthlyPlaylistSongsMapper.listMonthlyPlaylistSongsToListMonthlyPlaylistSongsDTO(monthlyPlaylistSongsRepository.findByMonthlyPlaylist(monthlyPlaylist));

    }

    @Override
    public MonthlyPlaylistSongsDTO save(MonthlyPlaylistSongsDTO monthlyPlaylistSongsDTO) {
        try{
            Optional<Song> optionalSong = songRepository.findById(monthlyPlaylistSongsDTO.getSongId());
            Optional<MonthlyPlaylist> optionalMonthlyPlaylist = monthlyPlaylistRepository.findById(monthlyPlaylistSongsDTO.getMonthlyPlaylistId());
            if(!optionalMonthlyPlaylist.isPresent() || !optionalSong.isPresent()){
                return null;
            }
            if(monthlyPlaylistSongsRepository.findBySongAndMonthlyPlaylist(optionalSong.get(), optionalMonthlyPlaylist.get()) != null){
                return null;
            }
            MonthlyPlaylistSongs monthlyPlaylistSongs = monthlyPlaylistSongsMapper.monthlyPlaylistSongsDTOToMonthlyPlaylistSongs(monthlyPlaylistSongsDTO);
            monthlyPlaylistSongs = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs);
            return monthlyPlaylistSongsMapper.monthlyPlaylistSongsToMonthlyPlaylistSongsDTO(monthlyPlaylistSongs);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public MonthlyPlaylistSongsDTO update(MonthlyPlaylistSongsDTO monthlyPlaylistDTO) {
        try{
            Optional<MonthlyPlaylistSongs> optionalMonthlyPlaylistSongs = monthlyPlaylistSongsRepository.findById(monthlyPlaylistDTO.getId());
            if(!optionalMonthlyPlaylistSongs.isPresent()){
                return null;
            }
            MonthlyPlaylistSongs monthlyPlaylistSongs = monthlyPlaylistSongsMapper.monthlyPlaylistSongsDTOToMonthlyPlaylistSongs(monthlyPlaylistDTO);
            return monthlyPlaylistSongsMapper.monthlyPlaylistSongsToMonthlyPlaylistSongsDTO(monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs));
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void disableAllSongs(Long idMonthlyPlaylist) {
        monthlyPlaylistSongsRepository.disableAllSongs(idMonthlyPlaylist);
    }

}
