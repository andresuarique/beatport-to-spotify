package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.model.SpotifyAccessToken;
import com.example.beatporttospotify.model.SpotifySong;
import com.example.beatporttospotify.model.SpotifySongList;
import com.example.beatporttospotify.service.SpotifyAPIService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Base64;

@Service
public class SpotifyAPIServiceImpl implements SpotifyAPIService {
    @Autowired
    Dotenv dotenv;

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
                .addParameter("redirect_uri", dotenv.get("SPOTIFY_CLIENT_SECRET"))
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
