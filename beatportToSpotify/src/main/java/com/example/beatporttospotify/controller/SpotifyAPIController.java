package com.example.beatporttospotify.controller;

import com.example.beatporttospotify.model.*;
import com.example.beatporttospotify.service.SpotifyAPIService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/spotify")
@CrossOrigin(origins = "*")
public class SpotifyAPIController {

@Autowired
private SpotifyAPIService spotifyAPIService;
private SpotifyAccessToken token = new SpotifyAccessToken();

@Value("${server.url}")
private String serverUrl;
@Value("${client.url}")
private String clientUrl;



    @GetMapping("/user")
    public ResponseEntity<?> getUser(){
        Instant currentTime = Instant.now();
        if(currentTime.isAfter(token.getToken_creation_time().plus(Duration.ofSeconds(token.getExpires_in()))))
            token= spotifyAPIService.refreshToken(token.getRefresh_token());

        SpotifyUser spotifyUser = spotifyAPIService.getUser(token.getAccess_token());
        return ResponseEntity.ok(spotifyUser);
    }
    @GetMapping("/token")
    public ResponseEntity<?> getToken(){
        token=spotifyAPIService.getToken();
        return ResponseEntity.ok(token);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(){
        try{
            String url = serverUrl+"/api/spotify/redirect";

            String scope = "user-read-currently-playing";
            scope = "user-read-private user-read-email user-read-currently-playing user-read-recently-played playlist-modify-public playlist-modify-private";
            System.out.println("callback");
            return ResponseEntity.ok(spotifyAPIService.authorize(url,scope));
        }catch (URISyntaxException e){
            return null;
        }
    }
    @GetMapping("/redirect")
    public RedirectView redirect(@RequestParam("code") String authorizationCode){
        String url = serverUrl+"/api/spotify/redirect";
        System.out.println("Redirect to: "+url);
        token = spotifyAPIService.exchangeAuthorizationCode(authorizationCode,url);
        if(token == null){
            System.out.println("Failed to obtain access token");
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(clientUrl +"/home");
        return redirectView;
    }
    @GetMapping("/search-song/{song}")
    public ResponseEntity<?> searchSong(@PathVariable String song){
        return ResponseEntity.ok(spotifyAPIService.searchSong(song));
    }
    @GetMapping("/create-playlist-by-name/{name}")
    public ResponseEntity<?> createPlaylist(@PathVariable String name){
        Instant currentTime = Instant.now();
        if(currentTime.isAfter(token.getToken_creation_time().plus(Duration.ofSeconds(token.getExpires_in()))))
            token= spotifyAPIService.refreshToken(token.getRefresh_token());

        SpotifyUser user = spotifyAPIService.getUser(token.getAccess_token());
        return  ResponseEntity.ok(spotifyAPIService.createPlaylist(name, user.getId(), token.getAccess_token()));
    }
    @PostMapping("/create-playlist")
    public ResponseEntity<?> createPlaylistFromBeatport(@RequestBody BeatportToSpotifyRequest request){
        Instant currentTime = Instant.now();
        if(currentTime.isAfter(token.getToken_creation_time().plus(Duration.ofSeconds(token.getExpires_in()))))
            token= spotifyAPIService.refreshToken(token.getRefresh_token());

        SpotifyUser user = spotifyAPIService.getUser(token.getAccess_token());
        return ResponseEntity.ok(spotifyAPIService.createPlaylistFromBeatport(request, user.getId(),token.getAccess_token()));
    }
}
