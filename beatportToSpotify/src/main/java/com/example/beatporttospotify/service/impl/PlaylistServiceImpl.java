package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.Playlist;
import com.example.beatporttospotify.dto.PlaylistDTO;
import com.example.beatporttospotify.mapper.PlaylistMapper;
import com.example.beatporttospotify.repository.PlaylistRepository;
import com.example.beatporttospotify.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    @Autowired
    private PlaylistRepository  playlistRepository;
    @Autowired
    private PlaylistMapper playlistMapper;
    @Override
    public List<PlaylistDTO> getPlaylists() {
        return playlistMapper.listPlaylistToListPlaylistDTO(playlistRepository.findAll());
    }

    @Override
    public PlaylistDTO getPlaylistById(Long id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if(!optionalPlaylist.isPresent()){
            return null;
        }
        return playlistMapper.playlistToPlaylistDTO(optionalPlaylist.get());
    }

    @Override
    public PlaylistDTO save(PlaylistDTO playlistDTO) {
        try{
            Playlist playlist = playlistMapper.playlistDTOToPlaylist(playlistDTO);
            playlist = playlistRepository.save(playlist);
            return playlistMapper.playlistToPlaylistDTO(playlist);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PlaylistDTO update(PlaylistDTO playlistDTO) {
        try{
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistDTO.getId());
            if(!optionalPlaylist.isPresent()){
                return null;
            }
            Playlist playlist = playlistMapper.playlistDTOToPlaylist(playlistDTO);
            return playlistMapper.playlistToPlaylistDTO(playlistRepository.save(playlist));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
