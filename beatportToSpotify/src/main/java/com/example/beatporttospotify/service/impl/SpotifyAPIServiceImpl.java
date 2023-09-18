package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.model.*;
import com.example.beatporttospotify.service.SpotifyAPIService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpotifyAPIServiceImpl implements SpotifyAPIService {
    @Autowired
    Dotenv dotenv;

    @Override
    public SpotifyUser getUser(String token){
        String url = "https://api.spotify.com/v1/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotifyUser> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SpotifyUser.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            SpotifyUser user = response.getBody();
            return user;
        } else {
            System.out.println("Failed to get user information. Status code: " + response.getStatusCode());
            return null;
        }
    }
    @Override
    public SpotifyAccessToken getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", dotenv.get("SPOTIFY_CLIENT_ID"));
        body.add("client_secret", dotenv.get("SPOTIFY_CLIENT_SECRET"));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<SpotifyAccessToken> responseEntity = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                requestEntity,
                SpotifyAccessToken.class
        );
        SpotifyAccessToken spotifyAccessToken = responseEntity.getBody();
        //System.out.printf("TOKEN:"+spotifyAccessToken.toString());
        return spotifyAccessToken;
    }

    @Override
    public RedirectView authorize(String url, String scope) throws URISyntaxException {
        String state = generateRandomString(16);

        URI authorizeUri = new URIBuilder("https://accounts.spotify.com/authorize")
                .addParameter("response_type", "code")
                .addParameter("client_id", dotenv.get("SPOTIFY_CLIENT_ID"))
                .addParameter("scope", scope)
                .addParameter("redirect_uri", url)
                .addParameter("state", state)
                .build();
        return new RedirectView(authorizeUri.toString());
    }

    @Override
    public SpotifyAccessToken exchangeAuthorizationCode(String authorizationCode, String url) {
        Instant creationTime = Instant.now();
        System.out.println(creationTime.plus(Duration.ofSeconds(3600)));
        String spotifyTokenUrl = "https://accounts.spotify.com/api/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(dotenv.get("SPOTIFY_CLIENT_ID"), dotenv.get("SPOTIFY_CLIENT_SECRET"));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", authorizationCode);
        body.add("redirect_uri", url);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<SpotifyAccessToken> response = restTemplate.exchange(
                spotifyTokenUrl,
                HttpMethod.POST,
                requestEntity,
                SpotifyAccessToken.class
        );
        System.out.println(response.getBody());
        SpotifyAccessToken tokenResponse = response.getBody();
        tokenResponse.setToken_creation_time(creationTime);
        return tokenResponse;
    }

    @Override
    public SpotifySong searchSong(String songName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.spotify.com/v1/search?q=" + encodeURL(songName)  +"&type=track";
        System.out.println("url = " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken().getAccess_token());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<SpotifySongList> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                SpotifySongList.class
        );

        SpotifySongList songList = responseEntity.getBody();
        return songList.getTracks().getItems().get(0);
    }

    @Override
    public SpotifyPlaylist createPlaylist(String name, String userId, String authorizationCode) {

        // Utilizar el token de acceso para crear la playlist
        String url = "https://api.spotify.com/v1/users/" + userId + "/playlists";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authorizationCode);
        headers.setContentType(MediaType.APPLICATION_JSON);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        String description = "Autogenerated Playlist on "+ formattedDate;
        String requestBody = "{\"name\": \"" + name + "\",\"description\": \""+description+"\",\"public\": true}";
        //System.out.println("requestBody = " + requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotifyPlaylist> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                SpotifyPlaylist.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
             System.out.println("Playlist created successfully!");
        } else {
              System.out.println("Failed to create playlist. Status code: " + response.getStatusCode());
        }
        SpotifyPlaylist spotifyPlaylist = response.getBody();
        return spotifyPlaylist;
    }

    @Override
    public String addSongs(Tracks tracks, String playlistId, String authorizationCode) {
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authorizationCode);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String songList= tracks.getItems().stream().map(song -> "\"spotify:track:"+song.getId()+"\"").collect(Collectors.joining(","));

        String requestBody = "{\"uris\": ["+ songList +"],\"position\": 0}";
        //System.out.println("requestBody = " + requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        System.out.println(requestBody);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("Songs are added successfully!");
        } else {
            System.out.println("Failed to add songs. Status code: " + response.getStatusCode());
        }
        //SpotifyPlaylist spotifyPlaylist = response.getBody();
        return response.getBody();


    }


    private String generateRandomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    public String encodeURL(String data){
        data=data.trim();
        /*
        data=data.toLowerCase();
        data=data.replace("/"," ");
        data=data.replace("#"," ");
        data=data.replace("&","and");

        try {
            data=URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }*/
        return data;
    }
}