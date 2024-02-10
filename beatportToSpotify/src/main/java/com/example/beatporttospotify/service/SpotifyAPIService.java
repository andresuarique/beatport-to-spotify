package com.example.beatporttospotify.service;

import com.example.beatporttospotify.dto.SongDTO;
import com.example.beatporttospotify.model.spotify.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface SpotifyAPIService {
    public SpotifyUser getUser(String token);
    public SpotifyAccessToken getToken();
    public Map<String,Object> authorize(String url, String scope) throws URISyntaxException;
    public SpotifyAccessToken exchangeAuthorizationCode (String authorizationCode, String url);
    public SpotifySong searchSong (String songName);
    public SpotifyPlaylist createPlaylist(String name, String userId, String authorizationCode);
    public String addSongs(Tracks tracks, String playlistId, String authorizationCode);
    public void addSongsFromDB(List<SongDTO> songDTOS, String playlistId, String authorizationCode);
    public SpotifyPlaylist getPlaylist(String token,String playlistId);
    public SpotifyAccessToken refreshToken(String refreshToken);
}
