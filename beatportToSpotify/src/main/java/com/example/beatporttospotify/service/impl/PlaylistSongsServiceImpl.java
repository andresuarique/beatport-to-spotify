package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.PlaylistSongs;
import com.example.beatporttospotify.dto.PlaylistSongsDTO;
import com.example.beatporttospotify.mapper.PlaylistSongsMapper;
import com.example.beatporttospotify.repository.PlaylistSongsRepository;
import com.example.beatporttospotify.service.PlaylistSongsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistSongsServiceImpl implements PlaylistSongsService {
    @Autowired
    private PlaylistSongsRepository playlistSongsRepository;
    @Autowired
    private PlaylistSongsMapper playlistSongsMapper;

    @Override
    public List<PlaylistSongsDTO> getPlaylistSongs() {
        return playlistSongsMapper.listPlaylistSongsToListPlaylistSongsDTO(playlistSongsRepository.findAll());
    }

    @Override
    public PlaylistSongsDTO getPlaylistSongsById(Long id) {
        Optional<PlaylistSongs> playlistSongsOptional = playlistSongsRepository.findById(id);
        if(!playlistSongsOptional.isPresent()){
            return null;
        }
        return playlistSongsMapper.playlistSongsToPlaylistSongsDTO(playlistSongsOptional.get());
    }

    @Override
    public PlaylistSongsDTO save(PlaylistSongsDTO playlistSongsDTO) {
        try{
            PlaylistSongs playlistSongs = playlistSongsMapper.playlistSongsDTOToPlaylistSongs(playlistSongsDTO);
            playlistSongs = playlistSongsRepository.save(playlistSongs);
            return playlistSongsMapper.playlistSongsToPlaylistSongsDTO(playlistSongs);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PlaylistSongsDTO update(PlaylistSongsDTO playlistSongsDTO) {
        try{
            Optional<PlaylistSongs> optionalPlaylistSongs = playlistSongsRepository.findById(playlistSongsDTO.getId());
            if(!optionalPlaylistSongs.isPresent()){
                return null;
            }
            PlaylistSongs playlistSongs = playlistSongsMapper.playlistSongsDTOToPlaylistSongs(playlistSongsDTO);
            return playlistSongsMapper.playlistSongsToPlaylistSongsDTO(playlistSongsRepository.save(playlistSongs));
        }catch (Exception e){
         e.printStackTrace();
         return null;
        }
    }
}
