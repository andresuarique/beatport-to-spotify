package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.domain.Playlist;
import com.example.beatporttospotify.dto.PlaylistDTO;
import com.example.beatporttospotify.mapper.GenreMapper;
import com.example.beatporttospotify.mapper.PlaylistMapper;
import com.example.beatporttospotify.repository.PlaylistRepository;
import com.example.beatporttospotify.service.GenreService;
import com.example.beatporttospotify.service.PlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private GenreService genreService;
    @Autowired
    private GenreMapper genreMapper;
    private static final Logger logger = LoggerFactory.getLogger(PlaylistServiceImpl.class);
    @Override
    public List<PlaylistDTO> getPlaylists() {
        return playlistMapper.listPlaylistToListPlaylistDTO(playlistRepository.findAll());
    }

    @Override
    public PlaylistDTO getPlaylistById(Long id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        return optionalPlaylist.map(playlist -> playlistMapper.playlistToPlaylistDTO(playlist)).orElse(null);
    }

    @Override
    public PlaylistDTO getPlaylistByGenre(String genreCode) {
        Genre genre = genreMapper.genreDTOToGenre(genreService.getGenreByCode(genreCode).get(0));
        Playlist playlist = playlistRepository.findByGenre(genre);
        return playlistMapper.playlistToPlaylistDTO(playlist);
    }

    @Override
    public PlaylistDTO save(PlaylistDTO playlistDTO) {
        try{
            Playlist playlist = playlistMapper.playlistDTOToPlaylist(playlistDTO);
            playlist = playlistRepository.save(playlist);
            return playlistMapper.playlistToPlaylistDTO(playlist);
        }catch (Exception e){
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
            return null;
        }
    }
}
