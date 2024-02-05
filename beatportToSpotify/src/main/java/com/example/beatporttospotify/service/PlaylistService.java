package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.PlaylistDTO;

import java.util.List;

public interface PlaylistService {
    public List<PlaylistDTO> getPlaylists();
    public PlaylistDTO getPlaylistById(Long id);
    public PlaylistDTO getPlaylistByGenre(String genreCode);
    public  PlaylistDTO save(PlaylistDTO playlistDTO);
    public  PlaylistDTO update(PlaylistDTO playlistDTO);
}
