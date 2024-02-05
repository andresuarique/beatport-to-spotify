package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.dto.PlaylistDTO;
import com.example.beatporttospotify.dto.PlaylistSongsDTO;
import com.example.beatporttospotify.dto.SongDTO;
import com.example.beatporttospotify.service.B2SService;
import com.example.beatporttospotify.service.PlaylistService;
import com.example.beatporttospotify.service.PlaylistSongsService;
import com.example.beatporttospotify.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class B2SServiceImpl implements B2SService {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistSongsService playlistSongsService;
    @Autowired
    private SongService songService;
    @Override
    public Map<String, Object> getPlaylistByGenreCode(String genreCode) {
        Map<String, Object> response = new HashMap<>();
        PlaylistDTO playlistDTO = playlistService.getPlaylistByGenre(genreCode);
        List<PlaylistSongsDTO> listPlaylistSongsDTO = playlistSongsService.getPlaylistSongsByPlaylist(playlistDTO);
        List<SongDTO> songDTOS = new ArrayList<>();
        listPlaylistSongsDTO.forEach(playlistSongsDTO -> {
            songDTOS.add(songService.getSongsById(playlistSongsDTO.getSongId()));
        });
        response.put("playlist",playlistDTO);
        response.put("songs",songDTOS);
        return response;
    }
}
