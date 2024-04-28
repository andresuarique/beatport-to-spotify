package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.PlaylistDTO;
import com.example.beatporttospotify.dto.PlaylistSongsDTO;
import com.example.beatporttospotify.dto.SongDTO;

import java.util.List;

public interface PlaylistSongsService {
    public List<PlaylistSongsDTO> getPlaylistSongs();
    public PlaylistSongsDTO getPlaylistSongsById(Long id);
    public List<PlaylistSongsDTO> getPlaylistSongsByPlaylist(PlaylistDTO playlistDTO);
    public PlaylistSongsDTO getPlaylistSongsByPlaylistAndSong(PlaylistDTO playlistDTO, SongDTO songDTO);
    public PlaylistSongsDTO save(PlaylistSongsDTO playlistSongsDTO);
    public PlaylistSongsDTO update(PlaylistSongsDTO playlistSongsDTO);
    public void disableAllSongs(Long idPlaylist);
}
