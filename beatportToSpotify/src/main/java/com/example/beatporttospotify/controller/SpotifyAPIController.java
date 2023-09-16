package com.example.beatporttospotify.controller;

import com.example.beatporttospotify.model.SpotifyAccessToken;
import com.example.beatporttospotify.service.SpotifyAPIService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyAPIController {
@Autowired
private Dotenv dotenv;
@Autowired
private SpotifyAPIService spotifyAPIService;

    @GetMapping("/token")
    public ResponseEntity<?> getToken(){
        return ResponseEntity.ok(spotifyAPIService.getToken());
    }

    @GetMapping("/callback")
    public RedirectView callback(){
        try{
            String url = "http://"+dotenv.get("SERVER_IP")+":"+dotenv.get("SERVER_PORT")+"/api/spotify/redirect";
            String scope = "user-read-currently-playing";
            scope = "user-read-private user-read-email user-read-currently-playing user-read-recently-played";
            return spotifyAPIService.authorize(url,scope);
        }catch (URISyntaxException e){
            return null;
        }
    }
    @GetMapping("/redirect")
    public RedirectView redirect(@RequestParam("code") String authorizationCode){
        String url = "http://"+dotenv.get("SERVER_IP")+":"+dotenv.get("SERVER_PORT")+"/api/spotify/redirect";
        SpotifyAccessToken spotifyAccessToken = spotifyAPIService.exchangeAuthorizationCode(authorizationCode,url);
        if(spotifyAccessToken == null){
            System.out.println("Failed to obtain access token");
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://"+dotenv.get("SERVER_IP")+":"+dotenv.get("SERVER_PORT")+"/api/spotify/redirect");
        return redirectView;
    }
    @GetMapping("/search-song/{song}")
    public ResponseEntity<?> searchSong(@PathVariable String song){
        return ResponseEntity.ok(spotifyAPIService.searchSong(song));
    }

}
