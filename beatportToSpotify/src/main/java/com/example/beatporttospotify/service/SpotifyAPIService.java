package com.example.beatporttospotify.service;

import com.example.beatporttospotify.model.*;
import org.springframework.web.servlet.view.RedirectView;

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
    public Map<String,Object> createPlaylistFromBeatport(BeatportToSpotifyRequest request, String userId, String authorizationCode);
    public SpotifyPlaylist getPlaylist(String token,String playlistId);
}
