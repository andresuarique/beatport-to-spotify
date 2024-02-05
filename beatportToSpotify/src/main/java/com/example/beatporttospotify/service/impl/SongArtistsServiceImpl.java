package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.SongArtists;
import com.example.beatporttospotify.dto.SongArtistsDTO;
import com.example.beatporttospotify.mapper.SongArtistsMapper;
import com.example.beatporttospotify.repository.SongArtistsRepository;
import com.example.beatporttospotify.service.SongArtistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongArtistsServiceImpl implements SongArtistsService {
    @Autowired
    private SongArtistsRepository songArtistsRepository;
    @Autowired
    private SongArtistsMapper songArtistsMapper;

    @Override
    public List<SongArtistsDTO> getSongsArtists() {
        return songArtistsMapper.listSongArtistToListSongArtistsDTO(songArtistsRepository.findAll());
    }

    @Override
    public SongArtistsDTO getSongsArtistsById(Long id) {
        Optional<SongArtists> optionalSongArtists = songArtistsRepository.findById(id);
        if(!optionalSongArtists.isPresent()){
            return null;
        }
        return songArtistsMapper.songArtistToSongArtistsDTO(optionalSongArtists.get());
    }

    @Override
    public SongArtistsDTO save(SongArtistsDTO songArtistsDTO) {
        try{
            SongArtists songArtists = songArtistsMapper.songArtistDTOToSongArtists(songArtistsDTO);
            songArtists = songArtistsRepository.save(songArtists);
            return songArtistsMapper.songArtistToSongArtistsDTO(songArtists);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SongArtistsDTO update(SongArtistsDTO songArtistsDTO) {
        try{
            Optional<SongArtists> optionalSongArtists = songArtistsRepository.findById(songArtistsDTO.getId());
            if(!optionalSongArtists.isPresent()){
                return null;
            }
            SongArtists songArtists = songArtistsMapper.songArtistDTOToSongArtists(songArtistsDTO);
            return songArtistsMapper.songArtistToSongArtistsDTO(songArtistsRepository.save(songArtists));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
