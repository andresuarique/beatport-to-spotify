package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.PlaylistSongsDTO;

import java.util.List;

public interface PlaylistSongsService {
    public List<PlaylistSongsDTO> getPlaylistSongs();
    public PlaylistSongsDTO getPlaylistSongsById(Long id);
    public PlaylistSongsDTO save(PlaylistSongsDTO playlistSongsDTO);
    public PlaylistSongsDTO update(PlaylistSongsDTO playlistSongsDTO);
}
