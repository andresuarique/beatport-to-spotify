package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.Artist;
import com.example.beatporttospotify.domain.Song;
import com.example.beatporttospotify.domain.SongArtists;
import com.example.beatporttospotify.dto.SongArtistsDTO;
import com.example.beatporttospotify.mapper.SongArtistsMapper;
import com.example.beatporttospotify.repository.ArtistRepository;
import com.example.beatporttospotify.repository.SongArtistsRepository;
import com.example.beatporttospotify.repository.SongRepository;
import com.example.beatporttospotify.service.SongArtistsService;

import java.util.ArrayList;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private ArtistRepository artistRepository;
    private static final Logger logger = LoggerFactory.getLogger(SongArtistsServiceImpl.class);


    @Override
    public List<SongArtistsDTO> getSongsArtists() {
        return songArtistsMapper.listSongArtistToListSongArtistsDTO(songArtistsRepository.findAll());
    }

    @Override
    public SongArtistsDTO getSongsArtistsById(Long id) {
        Optional<SongArtists> optionalSongArtists = songArtistsRepository.findById(id);
        return optionalSongArtists.map(songArtists -> songArtistsMapper.songArtistToSongArtistsDTO(songArtists)).orElse(null);
    }

    @Override
    public List<String> getSongsArtistsBySongId(Long songId) {
        Optional<Song> optionalSong = songRepository.findById(songId);
        if(!optionalSong.isPresent()){
            return Collections.emptyList();
        }
        List<SongArtists> songArtists = songArtistsRepository.findBySong(optionalSong.get());
        List<String> artists = new ArrayList<>();
        songArtists.forEach(songArtist -> artists.add(songArtist.getArtist().getBeatportName()));

        return artists;
    }

    @Override
    public SongArtistsDTO save(SongArtistsDTO songArtistsDTO) {
        try{
            Optional<Song> optionalSong = songRepository.findById(songArtistsDTO.getSongId());
            Optional<Artist> optionalArtist = artistRepository.findById(songArtistsDTO.getArtistId());
            if(!optionalArtist.isPresent() || !optionalSong.isPresent()){
                return null;
            }
            if(songArtistsRepository.findBySongAndArtist(optionalSong.get(), optionalArtist.get()) != null){
                return null;
            }
            SongArtists songArtists = songArtistsMapper.songArtistDTOToSongArtists(songArtistsDTO);
            songArtists = songArtistsRepository.save(songArtists);
            return songArtistsMapper.songArtistToSongArtistsDTO(songArtists);
        }catch (Exception e){
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
            return null;
        }
    }
}
