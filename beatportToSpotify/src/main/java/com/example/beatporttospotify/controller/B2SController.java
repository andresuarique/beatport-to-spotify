package com.example.beatporttospotify.controller;

import com.example.beatporttospotify.dto.B2SRequestDTO;
import com.example.beatporttospotify.model.spotify.SpotifyAccessToken;
import com.example.beatporttospotify.model.spotify.SpotifyUser;
import com.example.beatporttospotify.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "*")
public class B2SController {

    private SpotifyAccessToken token = new SpotifyAccessToken();

    @Value("${server.url}")
    private String serverUrl;
    @Value("${client.url}")
    private String clientUrl;

    @Autowired
    private SpotifyAPIService spotifyAPIService;

    @Autowired
    private GenreService genreService;
    @Autowired
    private B2SService b2SService;

    @GetMapping("/b2s/genres")
    public ResponseEntity<?> getGenres(){
        return ResponseEntity.ok(genreService.getGenres());
    }
    @GetMapping("/b2s/playlist/{genreName}/{genreCode}")
    public ResponseEntity<?> getPlaylist(@PathVariable("genreName")String genreName,@PathVariable("genreCode")String genreCode){
        return ResponseEntity.ok(b2SService.getPlaylistByGenreCode(genreCode));
    }
    @PostMapping("/b2s/create-playlist")
    public ResponseEntity<?> createPlaylist(@RequestBody B2SRequestDTO request){
        Instant currentTime = Instant.now();
        if(currentTime.isAfter(token.getToken_creation_time().plus(Duration.ofSeconds(token.getExpires_in()))))
            token= spotifyAPIService.refreshToken(token.getRefresh_token());

        SpotifyUser user = spotifyAPIService.getUser(token.getAccess_token());
        return ResponseEntity.ok(b2SService.createPlaylist(request,user.getId(), token.getAccess_token()));
    }

    @GetMapping("/api/spotify/user")
    public ResponseEntity<?> getUser(){
        Instant currentTime = Instant.now();
        if(currentTime.isAfter(token.getToken_creation_time().plus(Duration.ofSeconds(token.getExpires_in()))))
            token= spotifyAPIService.refreshToken(token.getRefresh_token());

        SpotifyUser spotifyUser = spotifyAPIService.getUser(token.getAccess_token());
        return ResponseEntity.ok(spotifyUser);
    }
    @GetMapping("/api/spotify/token")
    public ResponseEntity<?> getToken(){
        token=spotifyAPIService.getToken();
        return ResponseEntity.ok(token);
    }

    @GetMapping("/api/spotify/callback")
    public ResponseEntity<?> callback(){
        try{
            String url = serverUrl+"/api/spotify/redirect";

            String scope = "user-read-private user-read-email user-read-currently-playing user-read-recently-played playlist-modify-public playlist-modify-private";
            System.out.println("callback");
            return ResponseEntity.ok(spotifyAPIService.authorize(url,scope));
        }catch (URISyntaxException e){
            return null;
        }
    }
    @GetMapping("/api/spotify/redirect")
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
    @GetMapping("/api/spotify/search-song/{song}")
    public ResponseEntity<?> searchSong(@PathVariable String song){
        return ResponseEntity.ok(spotifyAPIService.searchSong(song));
    }
    @GetMapping("/api/spotify/create-playlist-by-name/{name}")
    public ResponseEntity<?> createPlaylist(@PathVariable String name){
        Instant currentTime = Instant.now();
        if(currentTime.isAfter(token.getToken_creation_time().plus(Duration.ofSeconds(token.getExpires_in()))))
            token= spotifyAPIService.refreshToken(token.getRefresh_token());

        SpotifyUser user = spotifyAPIService.getUser(token.getAccess_token());
        return  ResponseEntity.ok(spotifyAPIService.createPlaylist(name, user.getId(), token.getAccess_token()));
    }

        @Scheduled(cron = "0 0 0 * * *")
        @GetMapping("/scheduled-job/playlists-update")
        public ResponseEntity<?> updatePlaylist(){
            return ResponseEntity.ok(b2SService.updatePlaylist());
        }

}
