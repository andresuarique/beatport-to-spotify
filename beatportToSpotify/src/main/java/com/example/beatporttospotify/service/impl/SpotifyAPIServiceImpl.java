package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.dto.SongDTO;
import com.example.beatporttospotify.model.spotify.*;
import com.example.beatporttospotify.service.SpotifyAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpotifyAPIServiceImpl implements SpotifyAPIService {

    //@Autowired
    //private BeatportScrapperService beatportScrapperService;

    @Value("${spotify.client.id}")
    private String spotifyClientId;
    @Value("${spotify.client.secret}")
    private String spotifyClientSecret;
    private static final Logger logger = LoggerFactory.getLogger(SpotifyAPIServiceImpl.class);
    @Override
    public SpotifyUser getUser(String token){
        String url = "https://api.spotify.com/v1/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotifyUser> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SpotifyUser.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            logger.error("Failed to get user information. Status code: {}", response.getStatusCode());
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
        body.add("client_id", spotifyClientId);
        body.add("client_secret", spotifyClientSecret);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<SpotifyAccessToken> responseEntity = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                requestEntity,
                SpotifyAccessToken.class
        );
        return responseEntity.getBody();
    }

    @Override
    public Map<String,Object> authorize(String url, String scope){
        Map<String,Object> resp = new HashMap<>();
        try {
            String state = generateRandomString(16);

            URI authorizeUri = new URIBuilder("https://accounts.spotify.com/authorize")
                    .addParameter("response_type", "code")
                    .addParameter("client_id", spotifyClientId)
                    .addParameter("scope", scope)
                    .addParameter("redirect_uri", url)
                    .addParameter("state", state)
                    .build();

            resp.put("url", authorizeUri.toString());
            resp.put("success", true);
        }catch (Exception e){
            resp.put("success",e.getMessage());
            resp.put("error",e.getMessage());
        }
        return resp;
    }

    @Override
    public SpotifyAccessToken exchangeAuthorizationCode(String authorizationCode, String url) {
        Instant creationTime = Instant.now();
        String spotifyTokenUrl = "https://accounts.spotify.com/api/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(spotifyClientId, spotifyClientSecret);

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
        SpotifyAccessToken tokenResponse = response.getBody();
        if(tokenResponse != null){
            tokenResponse.setToken_creation_time(creationTime);
        }
        return tokenResponse;
    }

    @Override
    public SpotifySong searchSong(String songName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.spotify.com/v1/search?q=" + encodeURL(songName)  +"&type=artist,track";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken().getAccess_token());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<SpotifySongList> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                SpotifySongList.class
        );
        if(responseEntity.getBody() != null){
        SpotifySongList songList = responseEntity.getBody();
        if(songList != null && !songList.getTracks().getItems().isEmpty()){
            return songList.getTracks().getItems().get(0);
        }
        }
        return null;
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
        String requestBody = "{\"name\": \"" + name + "\",\"description\": \""+description+"\",\"public\": false}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotifyPlaylist> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                SpotifyPlaylist.class);

        System.out.println("== "+restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class).getBody());
        if (response.getStatusCode() == HttpStatus.CREATED) {
            logger.info("Playlist created successfully!");
        } else {
            logger.error("Failed to create playlist. Status code: {}", response.getStatusCode());
        }
        SpotifyPlaylist spotifyPlaylist  = response.getBody();
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
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            logger.info("Songs are added successfully!");
        } else {
            logger.error("Failed to add songs. Status code: {}", response.getStatusCode());
        }
        return response.getBody();
    }

    @Override
    public void addSongsFromDB(List<SongDTO> songDTOS, String playlistId, String authorizationCode) {
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authorizationCode);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String songList= songDTOS.stream().map(song -> "\"spotify:track:"+song.getSpotifyId()+"\"").collect(Collectors.joining(","));

        String requestBody = "{\"uris\": ["+ songList +"],\"position\": 0}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            logger.info("Songs are added successfully!");
        } else {
            logger.error("Failed to add songs. Status code: {}", response.getStatusCode());
        }
        response.getBody();
    }

    @Override
    public SpotifyPlaylist getPlaylist(String token,String playlistId){
        String url = "https://api.spotify.com/v1/playlists/"+playlistId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotifyPlaylist> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SpotifyPlaylist.class);
        System.out.println("-- "+restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class).getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            logger.error("Failed to get playlist information. Status code: {}", response.getStatusCode());
            return null;
        }
    }
    @Override
    public SpotifyAccessToken refreshToken(String refreshToken) {
        String clientId = spotifyClientId;
        String clientSecret = spotifyClientSecret;
        Instant creationTime = Instant.now();
        String spotifyTokenUrl = "https://accounts.spotify.com/api/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<SpotifyAccessToken> response = restTemplate.exchange(
                spotifyTokenUrl,
                HttpMethod.POST,
                requestEntity,
                SpotifyAccessToken.class
        );

        SpotifyAccessToken tokenResponse = response.getBody();
        if(tokenResponse != null){
            tokenResponse.setToken_creation_time(creationTime);
            tokenResponse.setRefresh_token(refreshToken);
        }
        return tokenResponse;
    }

    private String generateRandomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    private String encodeURL(String data){
        data=data.toLowerCase();
        data=data.replace("/"," ");
        data=data.replace("#"," ");
        data=data.replace("&","and");
        data=data.trim();
        return data;
    }
}
