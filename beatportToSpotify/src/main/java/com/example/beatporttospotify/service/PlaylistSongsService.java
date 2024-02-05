package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.PlaylistDTO;
import com.example.beatporttospotify.dto.PlaylistSongsDTO;

import java.util.List;

public interface PlaylistSongsService {
    public List<PlaylistSongsDTO> getPlaylistSongs();
    public PlaylistSongsDTO getPlaylistSongsById(Long id);
    public List<PlaylistSongsDTO> getPlaylistSongsByPlaylist(PlaylistDTO playlistDTO);
    public PlaylistSongsDTO save(PlaylistSongsDTO playlistSongsDTO);
    public PlaylistSongsDTO update(PlaylistSongsDTO playlistSongsDTO);
}
