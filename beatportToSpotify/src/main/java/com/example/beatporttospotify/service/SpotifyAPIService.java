package com.example.beatporttospotify.service;

import com.example.beatporttospotify.model.SpotifyAccessToken;
import com.example.beatporttospotify.model.SpotifySong;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URISyntaxException;

public interface SpotifyAPIService {
    public SpotifyAccessToken getToken();
    public RedirectView authorize(String url, String scope) throws URISyntaxException;
    public SpotifyAccessToken exchangeAuthorizationCode (String authorizationCode, String url);
    public SpotifySong searchSong (String songName);
}
