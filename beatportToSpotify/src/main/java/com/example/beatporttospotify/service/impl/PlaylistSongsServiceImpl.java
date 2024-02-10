package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.Playlist;
import com.example.beatporttospotify.domain.PlaylistSongs;
import com.example.beatporttospotify.domain.Song;
import com.example.beatporttospotify.dto.PlaylistDTO;
import com.example.beatporttospotify.dto.PlaylistSongsDTO;
import com.example.beatporttospotify.mapper.PlaylistMapper;
import com.example.beatporttospotify.mapper.PlaylistSongsMapper;
import com.example.beatporttospotify.repository.PlaylistRepository;
import com.example.beatporttospotify.repository.PlaylistSongsRepository;
import com.example.beatporttospotify.repository.SongRepository;
import com.example.beatporttospotify.service.PlaylistService;
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
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistMapper playlistMapper;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private SongRepository songRepository;

    @Override
    public List<PlaylistSongsDTO> getPlaylistSongs() {
        return playlistSongsMapper.listPlaylistSongsToListPlaylistSongsDTO(playlistSongsRepository.findAll());
    }

    @Override
    public List<PlaylistSongsDTO> getPlaylistSongsByPlaylist(PlaylistDTO playlistDTO) {
        Playlist playlist = playlistMapper.playlistDTOToPlaylist(playlistService.getPlaylistById(playlistDTO.getId()));
        if(playlist == null){
            return null;
        }
        return playlistSongsMapper.listPlaylistSongsToListPlaylistSongsDTO(playlistSongsRepository.findByPlaylist(playlist));
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
            Optional<Song> optionalSong = songRepository.findById(playlistSongsDTO.getSongId());
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistSongsDTO.getPlaylistId());
            if(!optionalPlaylist.isPresent() || !optionalSong.isPresent()){
                return null;
            }
            if(playlistSongsRepository.findBySongAndPlaylist(optionalSong.get(), optionalPlaylist.get()) != null){
                return null;
            }
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

    @Override
    public boolean disableAllSongs(Long idPlaylist) {
        playlistSongsRepository.disableAllSongs(idPlaylist);
        return true;
    }
}
