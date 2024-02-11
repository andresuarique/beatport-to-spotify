package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.Song;
import com.example.beatporttospotify.dto.SongDTO;
import com.example.beatporttospotify.mapper.SongMapper;
import com.example.beatporttospotify.repository.SongRepository;
import com.example.beatporttospotify.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongServiceImpl implements SongService {
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private SongMapper songMapper;
    private static final Logger logger = LoggerFactory.getLogger(SongServiceImpl.class);
    @Override
    public List<SongDTO> getSongs() {
        return songMapper.listSongToListSongDTO(songRepository.findAll());
    }

    @Override
    public SongDTO getSongsById(Long id) {
        Optional<Song> optionalSong = songRepository.findById(id);
        return optionalSong.map(song -> songMapper.songToSongDTO(song)).orElse(null);
    }
    @Override
    public List<SongDTO> getSongeByName(String name) {
        return songMapper.listSongToListSongDTO(songRepository.findByBeatportName(name));
    }
    @Override
    public SongDTO save(SongDTO songDTO) {
        try{
            List<SongDTO> songDTOS = getSongeByName(songDTO.getBeatportName());
            if(!songDTOS.isEmpty())
                return songDTOS.get(0);

            Song song = songMapper.songDTOToSong(songDTO);
            song = songRepository.save(song);
            return songMapper.songToSongDTO(song);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public SongDTO update(SongDTO songDTO) {
        try{
            Optional<Song> optionalSong = songRepository.findById(songDTO.getId());
            if(!optionalSong.isPresent())
                return null;
            Song song = songMapper.songDTOToSong(songDTO);
            return songMapper.songToSongDTO(songRepository.save(song));
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }
}
