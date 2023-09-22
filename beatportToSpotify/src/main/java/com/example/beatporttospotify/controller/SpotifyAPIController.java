package com.example.beatporttospotify.controller;

import com.example.beatporttospotify.model.*;
import com.example.beatporttospotify.service.SpotifyAPIService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/spotify")
@CrossOrigin(origins = "*")
public class SpotifyAPIController {
@Autowired
private Dotenv dotenv;
@Autowired
private SpotifyAPIService spotifyAPIService;
private SpotifyAccessToken token = new SpotifyAccessToken();




    @GetMapping("/user")
    public ResponseEntity<?> getUser(){

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
            String url = dotenv.get("SERVER_URL")+"/api/spotify/redirect";

            String scope = "user-read-currently-playing";
            scope = "user-read-private user-read-email user-read-currently-playing user-read-recently-played playlist-modify-public playlist-modify-private";
            //scope = "user-read-private user-read-email";
            System.out.println("callback");
            return ResponseEntity.ok(spotifyAPIService.authorize(url,scope));
        }catch (URISyntaxException e){
            return null;
        }
    }
    @GetMapping("/redirect")
    public RedirectView redirect(@RequestParam("code") String authorizationCode){
        System.out.println("redirect");
        String url = dotenv.get("SERVER_URL")+"/api/spotify/redirect";
        System.out.println(url);
        token = spotifyAPIService.exchangeAuthorizationCode(authorizationCode,url);
        if(token == null){
            System.out.println("Failed to obtain access token");
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(dotenv.get("CLIENT_URL")+"/home/logged");
        System.out.println("redirect fin");
        return redirectView;
    }
    @GetMapping("/search-song/{song}")
    public ResponseEntity<?> searchSong(@PathVariable String song){

        return ResponseEntity.ok(spotifyAPIService.searchSong(song));
    }
    @GetMapping("/create-playlist-by-name/{name}")
    public ResponseEntity<?> createPlaylist(@PathVariable String name){
        System.out.println("GET");
        SpotifyUser user = spotifyAPIService.getUser(token.getAccess_token());
        return  ResponseEntity.ok(spotifyAPIService.createPlaylist(name, user.getId(), token.getAccess_token()));
    }
    @PostMapping("/create-playlist")
    public ResponseEntity<?> createPlaylistFromBeatport(@RequestBody BeatportToSpotifyRequest request){
        System.out.println("POST");
        SpotifyUser user = spotifyAPIService.getUser(token.getAccess_token());
        spotifyAPIService.createPlaylistFromBeatport(request, user.getId(),token.getAccess_token());
        return ResponseEntity.ok(null);
    }
}
