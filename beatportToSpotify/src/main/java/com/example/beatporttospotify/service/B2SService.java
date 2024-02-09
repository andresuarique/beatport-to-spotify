package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.B2SRequestDTO;

import java.util.Map;

public interface B2SService {
    public Map<String, Object> getPlaylistByGenreCode(String genreCode);
    public Map<String, Object> createPlaylist(B2SRequestDTO b2SRequestDTO,String userid, String token);
    public Map<String, Object> updatePlaylist();
}
