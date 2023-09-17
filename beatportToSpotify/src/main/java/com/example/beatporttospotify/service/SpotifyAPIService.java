package com.example.beatporttospotify.service;

import com.example.beatporttospotify.model.SpotifyAccessToken;
import com.example.beatporttospotify.model.SpotifyPlaylist;
import com.example.beatporttospotify.model.SpotifySong;
import com.example.beatporttospotify.model.SpotifyUser;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URISyntaxException;

public interface SpotifyAPIService {
    public SpotifyUser getUser(String token);
    public SpotifyAccessToken getToken();
    public RedirectView authorize(String url, String scope) throws URISyntaxException;
    public SpotifyAccessToken exchangeAuthorizationCode (String authorizationCode, String url);
    public SpotifySong searchSong (String songName);
    public SpotifyPlaylist createPlaylist(String name, String userId, String authorizationCode);
}
