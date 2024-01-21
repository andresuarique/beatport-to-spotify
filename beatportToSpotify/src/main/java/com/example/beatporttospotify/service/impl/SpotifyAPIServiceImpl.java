package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.model.*;
import com.example.beatporttospotify.service.BeatportScrapperService;
import com.example.beatporttospotify.service.SpotifyAPIService;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;

import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpotifyAPIServiceImpl implements SpotifyAPIService {

    @Autowired
    private BeatportScrapperService beatportScrapperService;

    @Value("${spotify.client.id}")
    private String spotifyClientId;
    @Value("${spotify.client.secret}")
    private String spotifyClientSecret;
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
        body.add("client_id", spotifyClientId);
        body.add("client_secret", spotifyClientSecret);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<SpotifyAccessToken> responseEntity = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                requestEntity,
                SpotifyAccessToken.class
        );
        SpotifyAccessToken spotifyAccessToken = responseEntity.getBody();
        return spotifyAccessToken;
    }

    @Override
    public Map<String,Object> authorize(String url, String scope) throws URISyntaxException {
        Map<String,Object> resp = new HashMap<>();
        String state = generateRandomString(16);

        URI authorizeUri = new URIBuilder("https://accounts.spotify.com/authorize")
                .addParameter("response_type", "code")
                .addParameter("client_id", spotifyClientId)
                .addParameter("scope", scope)
                .addParameter("redirect_uri", url)
                .addParameter("state", state)
                .build();

        System.out.println("callback1");
        System.out.println(authorizeUri);
        resp.put("url",authorizeUri.toString());
        return resp;
    }

    @Override
    public SpotifyAccessToken exchangeAuthorizationCode(String authorizationCode, String url) {
        Instant creationTime = Instant.now();
        System.out.println(creationTime.plus(Duration.ofSeconds(3600)));
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
        System.out.println(response.getBody());
        SpotifyAccessToken tokenResponse = response.getBody();
        tokenResponse.setToken_creation_time(creationTime);
        return tokenResponse;
    }

    @Override
    public SpotifySong searchSong(String songName) {
        System.out.println("Song: "+songName);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.spotify.com/v1/search?q=" + encodeURL(songName)  +"&type=artist,track";
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
        ResponseEntity<Object> a = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );
        System.out.println("response: "+(Map<String,Object>) a.getBody());
        if(responseEntity.getBody() != null){
        SpotifySongList songList = responseEntity.getBody();
        System.out.println(songList.getTracks().getItems().get(0).getAlbum().getImages().get(0));
        return songList.getTracks().getItems().get(0);
        }
        return null;
    }

    @Override
    public SpotifyPlaylist createPlaylist(String name, String userId, String authorizationCode) {
        System.out.println("playlist: "+name);
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

    @Override
    public Map<String,Object> createPlaylistFromBeatport(BeatportToSpotifyRequest request, String userId, String authorizationCode) {
        Map<String,Object> response = new HashMap<>();
        SpotifyPlaylist spotifyPlaylist = createPlaylist(request.getPlaylistName() ,userId,authorizationCode);
        if(request.getSongs() == null || request.getSongs().isEmpty()) {
            System.out.println("songs null");
            request.setSongs(beatportScrapperService.getTop100(request.getGenre()));
        }
        Tracks tracks = new Tracks();
        List<SpotifySong> songs = new ArrayList<>();
        List<BeatportSong> notFound = new ArrayList<>();
        request.getSongs().forEach(beatportSong ->{
            SpotifySong song =  searchSong(beatportSong.getName()+" "+clearArtistText(beatportSong.getArtists().get(0)));
            if(song != null){
                songs.add(song);
            }
            else {
                notFound.add(beatportSong);
            }
        });
        tracks.setItems(songs);
        addSongs(tracks,spotifyPlaylist.getId(),authorizationCode);
        spotifyPlaylist=getPlaylist(authorizationCode,spotifyPlaylist.getId());
        response.put("playlist",spotifyPlaylist);
        response.put("notFound",notFound);
        response.put("tracks",tracks);
        return response;
    }
    @Override
    public SpotifyPlaylist getPlaylist(String token,String playlistId){
        String url = "https://api.spotify.com/v1/playlists/"+playlistId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SpotifyPlaylist> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SpotifyPlaylist.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            SpotifyPlaylist playlist = response.getBody();
            return playlist;
        } else {
            System.out.println("Failed to get playlist information. Status code: " + response.getStatusCode());
            return null;
        }
    }
    @Override
    public SpotifyAccessToken refreshToken(String refreshToken) {
        String clientId = spotifyClientId;
        String clientSecret = spotifyClientSecret;
        System.out.println(" REFRESH TOKEN: "+refreshToken);
        Instant creationTime = Instant.now();
        System.out.println("time: "+creationTime);
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
        tokenResponse.setToken_creation_time(creationTime);
        tokenResponse.setRefresh_token(refreshToken);
        System.out.println("RT: "+tokenResponse.getToken_creation_time());
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
/*
        try {
            data= URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return data;

        }*/
        return data;
    }
    private String clearArtistText(String artist){
        artist = artist.trim();
        artist = StringEscapeUtils.unescapeHtml4(artist);
        //(ofc) (US) (OZ) (ITA) (UK) (BR)
        String array[]={"(ofc)","(US)","(UK)","(ITA)","(OZ)","(BR)","(IT)","(UZ)","(FR)","(ES)","(YU)",
                "(CA)","(PL)","(BE)","(SE)","(DE)","(PT)","(NO)","(RSA)","(SA)","(RO)","(Havana)","(VE)","(UZ)"
                ,"(FL)","(AR)","(EON)","(BG)","(ZM)","(TN)","(NL)","(Palestina)","(GB)","(Official)","(CO)","(Aus)"
                ,"(DnB)","(MX)","(HU)","(fr)","(It)","(SC)","(CZ)","(JP)","(SWE)","(IL)","(AZ)","(TN)","(NYC)","(Italy)"
                ,"(LA)","(AUS)","(ARG)","(PE)","(GER)","(ESP)","(AU)","(SL)","(BRA)","(MU)","(Paris)","(UA)","(IND)","(CU)","(GR)"
                ,"(Brazil)","(ISR)","(Psy)","(IE)","(LU)","(CL)","(UY)","(JPN)"};
        for (String data: array) {
                    if(artist.contains(data)){
                        artist=artist.replace(data,"");
                        break;
                    }
        }
        if(artist.contains("(") && artist.contains(")")){
            System.out.println("ARTIST : "+artist);
        }
        return artist;
    }
}
