package com.example.beatporttospotify.controller;

import com.example.beatporttospotify.dto.B2SRequestDTO;
import com.example.beatporttospotify.model.spotify.SpotifyAccessToken;
import com.example.beatporttospotify.model.spotify.SpotifyUser;
import com.example.beatporttospotify.service.*;
import com.example.beatporttospotify.service.impl.GenreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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

    private static  final Logger logger = LoggerFactory.getLogger(B2SController.class);
    @GetMapping("/b2s/genres")
    public ResponseEntity<?> getGenres(){
        Map<String, Object> response = new HashMap<>();
        try{
            response.put("success",true);
            response.put("genres",genreService.getGenres());
        }catch (Exception e){
            response.put("success",false);
            response.put("error",e.getMessage());
        }
        return ResponseEntity.ok(response);
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
        Map<String,Object> response = new HashMap<>();
        try {
            Instant currentTime = Instant.now();
            if (currentTime.isAfter(token.getToken_creation_time().plus(Duration.ofSeconds(token.getExpires_in()))))
                token = spotifyAPIService.refreshToken(token.getRefresh_token());

            SpotifyUser spotifyUser = spotifyAPIService.getUser(token.getAccess_token());
            response.put("success",true);
            response.put("spotifyUser",spotifyUser);
        }catch (Exception e){
            response.put("success",false);
            response.put("error",e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/api/spotify/token")
    public ResponseEntity<?> getToken(){
        Map<String,Object> response = new HashMap<>();
        try {
            token = spotifyAPIService.getToken();
            response.put("success",true);
            response.put("token",token);
        }catch (Exception e){
            response.put("success",false);
            response.put("error",e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/spotify/callback")
    public ResponseEntity<?> callback(){
            String url = serverUrl+"/api/spotify/redirect";
            String scope = "user-read-private user-read-email user-read-currently-playing user-read-recently-played playlist-modify-public playlist-modify-private";
            return ResponseEntity.ok(spotifyAPIService.authorize(url,scope));
    }
    @GetMapping("/api/spotify/redirect")
    public RedirectView redirect(@RequestParam("code") String authorizationCode){
        Map<String,Object> response = new HashMap<>();
        String url = serverUrl+"/api/spotify/redirect";
        token = spotifyAPIService.exchangeAuthorizationCode(authorizationCode,url);
        if(token == null){
            logger.error("Failed to obtain access token");
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(clientUrl +"/home");
        return redirectView;
    }
    @GetMapping("/api/spotify/search-song/{song}")
    public ResponseEntity<?> searchSong(@PathVariable String song){
        Map<String,Object> response = new HashMap<>();
        try{
            response.put("song",spotifyAPIService.searchSong(song));
            response.put("success",true);
        }catch (Exception e){
            response.put("error",e.getMessage());
            response.put("success",false);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/api/spotify/create-playlist-by-name/{name}")
    public ResponseEntity<?> createPlaylist(@PathVariable String name){
        Map<String,Object> response = new HashMap<>();
        try {
            Instant currentTime = Instant.now();
            if (currentTime.isAfter(token.getToken_creation_time().plus(Duration.ofSeconds(token.getExpires_in()))))
                token = spotifyAPIService.refreshToken(token.getRefresh_token());

            SpotifyUser user = spotifyAPIService.getUser(token.getAccess_token());
            response.put("playlist",spotifyAPIService.createPlaylist(name, user.getId(), token.getAccess_token()));
            response.put("success",true);
        }catch (Exception e){
            response.put("error",e.getMessage());
            response.put("success",false);
        }
        return  ResponseEntity.ok(response);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @GetMapping("/scheduled-job/playlists-update")
    public ResponseEntity<?> updatePlaylist(){
        return ResponseEntity.ok(b2SService.updatePlaylist());
    }

}
