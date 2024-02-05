package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.dto.*;
import com.example.beatporttospotify.model.scraper.BeatportGenre;
import com.example.beatporttospotify.model.scraper.BeatportSong;
import com.example.beatporttospotify.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BeatportScrapperServiceImpl implements BeatportScrapperService {
    @Autowired
    private GenreService genreService;
    @Autowired
    private SongService songService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private SongArtistsService songArtistsService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistSongsService playlistSongsService;
    @Override
    public Document getHTML(String url) {
        try {
        System.out.println("get HTML from: " + url);
        Connection.Response response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
        Document document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        return document;
        }catch (Exception e){
        e.printStackTrace();
        return null;
        }

    }

    @Override
    public List<BeatportGenre> getGenres() {
        Elements songs = getHTML("https://www.beatport.com/charts/all").select("script#__NEXT_DATA__");

        String json = songs.get(0).html();
        JSONObject jsonObject = new JSONObject(json);
        String json2= jsonObject.getJSONObject("props")
                .getJSONObject("pageProps")
                .getJSONObject("dehydratedState").getJSONArray("queries").getJSONObject(0).toString();

        jsonObject = new JSONObject(json2);
        json2 = jsonObject.getJSONObject("state").getJSONObject("data").getJSONObject("facets").getJSONObject("fields").getJSONArray("genre").toString();
        JSONArray jsonArray = new JSONArray(json2);
        List<BeatportGenre> genreList = new ArrayList<>();

        GenreDTO genreDTO = new GenreDTO();
        for(int i = 0; i<jsonArray.length();i++){
        JSONObject object = jsonArray.getJSONObject(i);
        BeatportGenre beatportGenre = new BeatportGenre();
        Long id =0L;
            try {
                id = Long.parseLong(object.get("id").toString());
            }catch (Exception e){
            }
        beatportGenre.setId(id);
        beatportGenre.setName(object.get("name").toString());
        beatportGenre.setUrl(stringFormatter(beatportGenre.getName())+"/"+beatportGenre.getId());

        genreDTO.setCode(String.valueOf(id));
        genreDTO.setName(object.get("name").toString());
        genreDTO.setUrl(stringFormatter(beatportGenre.getName())+"/"+beatportGenre.getId());
        genreDTO.setStatus(GenreDTO.ENABLE);
        genreService.save(genreDTO);

        genreList.add(beatportGenre);
        }
        return genreList;
    }

    @Override
    public List<BeatportSong> getTop100(String genre) {
        String url="https://www.beatport.com/genre/"+genre+"/top-100";
        Elements songs;
        if(genre.equals("general/0")){
            url="https://www.beatport.com/top-100";
        }
        System.out.println(genre.split("/")[1]);
        GenreDTO genreDTO = genreService.getGenreByCode(genre.split("/")[1]);
        if(genreDTO == null){
            System.out.println("no hay genero");
            return null;
        }
        System.out.println("urlScrapper: "+url);
        Document html =getHTML(url);
         songs = html.select("script#__NEXT_DATA__");
        String json = songs.get(0).html();
        JSONObject jsonObject = new JSONObject(json);
        String json2= jsonObject.getJSONObject("props")
                .getJSONObject("pageProps")
                .getJSONObject("dehydratedState").getJSONArray("queries").getJSONObject(0).toString();

        jsonObject = new JSONObject(json2);
        json2 = jsonObject.getJSONObject("state").getJSONObject("data").getJSONArray("results").toString();
        JSONArray jsonArray = new JSONArray(json2);
        List<BeatportSong> beatportSongList = new ArrayList<>();
        SongDTO songDTO = new SongDTO();
        List<ArtistDTO> artistDTOS = new ArrayList<>();
        ArtistDTO artistDTO = new ArtistDTO();
        SongArtistsDTO songArtistsDTO = new SongArtistsDTO();
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setName(genreDTO.getName() + " top 100");
        playlistDTO.setGenreId(genreDTO.getId());
        playlistDTO.setCreationDate(new Date());
        playlistDTO = playlistService.save(playlistDTO);

        PlaylistSongsDTO playlistSongsDTO = new PlaylistSongsDTO();
        for(int i = 0; i<jsonArray.length();i++){
            playlistSongsDTO = new PlaylistSongsDTO();
            songDTO = new SongDTO();
            artistDTOS.clear();
            JSONObject object = jsonArray.getJSONObject(i);
            BeatportSong beatportSong = new BeatportSong();
            String songName = object.get("name").toString();
            String songImage = object.getJSONObject("release").getJSONObject("image").get("uri").toString();


            songDTO.setBeatportName(songName);
            songDTO.setBeatportImageUrl(songImage);
            songDTO.setStatus("ENABLE");
            songDTO = songService.save(songDTO);

            List<String> artists = new ArrayList<>();
            JSONArray array = object.getJSONArray("artists");
            for(int j = 0; j<array.length();j++){
                artistDTO = new ArtistDTO();
                JSONObject artistObject = array.getJSONObject(j);
                artists.add(artistObject.get("name").toString());

                artistDTO.setBeatportName(artistObject.get("name").toString());
                artistDTO = artistService.save(artistDTO);
                artistDTOS.add(artistDTO);
                songArtistsDTO.setArtistId(artistDTO.getId());
                songArtistsDTO.setSongId(songDTO.getId());
                songArtistsService.save(songArtistsDTO);
            }
            beatportSong.setName(songName);
            beatportSong.setUrlImage(songImage);
            beatportSong.setArtists(artists);
            beatportSongList.add(beatportSong);

            playlistSongsDTO.setSongId(songDTO.getId());
            playlistSongsDTO.setPlaylistId(playlistDTO.getId());
            playlistSongsDTO.setStatus("ENABLE");
            playlistSongsDTO = playlistSongsService.save(playlistSongsDTO);
        }
        return beatportSongList;
    }
    private String stringFormatter(String input){
        String lowercase = input.toLowerCase();
        String removedCharacters = lowercase.replaceAll("[()&/]", "");
        String replacedSpaces = removedCharacters.replaceAll("\\s+", "-");
        return replacedSpaces;
    }

}
