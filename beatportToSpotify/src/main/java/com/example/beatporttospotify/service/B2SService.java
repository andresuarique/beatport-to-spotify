package com.example.beatporttospotify.service;

import java.util.Map;

public interface B2SService {
    public Map<String, Object> getPlaylistByGenreCode(String genreCode);
}
