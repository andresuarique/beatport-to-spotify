package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.dto.*;
import com.example.beatporttospotify.model.scraper.BeatportGenre;
import com.example.beatporttospotify.model.scraper.BeatportSong;
import com.example.beatporttospotify.model.spotify.SpotifySong;
import com.example.beatporttospotify.service.*;
import org.apache.commons.lang3.StringEscapeUtils;
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
    @Autowired
    private SpotifyAPIService spotifyAPIService;

    @Override
    public Document getHTML(String url) {
        try {
            return Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<BeatportGenre> getGenres() {
        Elements songs = getHTML("https://www.beatport.com/charts/all").select("script#__NEXT_DATA__");

        String json = songs.get(0).html();
        JSONObject jsonObject = new JSONObject(json);
        String json2 = jsonObject.getJSONObject("props")
                .getJSONObject("pageProps")
                .getJSONObject("dehydratedState").getJSONArray("queries").getJSONObject(0).toString();

        jsonObject = new JSONObject(json2);
        json2 = jsonObject.getJSONObject("state").getJSONObject("data").getJSONObject("facets").getJSONObject("fields").getJSONArray("genre").toString();
        JSONArray jsonArray = new JSONArray(json2);
        List<BeatportGenre> genreList = new ArrayList<>();

        GenreDTO genreDTO = new GenreDTO();
        JSONObject object;
        BeatportGenre beatportGenre;
        for (int i = 0; i < jsonArray.length(); i++) {
            object = jsonArray.getJSONObject(i);
            beatportGenre = new BeatportGenre();
            Long id = 0L;
            try {
                id = Long.parseLong(object.get("id").toString());
            } catch (Exception e) {
            }
            beatportGenre.setId(id);
            beatportGenre.setName(object.get("name").toString());
            beatportGenre.setUrl(stringFormatter(beatportGenre.getName()) + "/" + beatportGenre.getId());

            genreDTO.setCode(String.valueOf(id));
            genreDTO.setName(object.get("name").toString());
            genreDTO.setUrl(stringFormatter(beatportGenre.getName()) + "/" + beatportGenre.getId());
            genreDTO.setStatus(GenreDTO.ENABLE);
            genreService.save(genreDTO);
            genreList.add(beatportGenre);
        }
        return genreList;
    }

    @Override
    public List<BeatportSong> getTop100(String genre) {
        String url = "https://www.beatport.com/genre/" + genre + "/top-100";
        Elements songs;
        if (genre.equals("General/0")) {
            url = "https://www.beatport.com/top-100";
        }
        List<GenreDTO> genresDTO = genreService.getGenreByNameAndCode(genre.split("/")[0],genre.split("/")[1]);
        if (genresDTO.isEmpty() ) {
            return null;
        }
        GenreDTO genreDTO = genresDTO.get(0);
        Document html = getHTML(url);
        songs = html.select("script#__NEXT_DATA__");
        String json = songs.get(0).html();
        JSONObject jsonObject = new JSONObject(json);
        String json2 = jsonObject.getJSONObject("props")
                .getJSONObject("pageProps")
                .getJSONObject("dehydratedState").getJSONArray("queries").getJSONObject(0).toString();

        jsonObject = new JSONObject(json2);
        json2 = jsonObject.getJSONObject("state").getJSONObject("data").getJSONArray("results").toString();
        JSONArray jsonArray = new JSONArray(json2);

        PlaylistDTO playlistDTO = playlistService.getPlaylistByGenre(genreDTO.getCode());
        if(playlistDTO != null){
            playlistDTO.setModificationDate(new Date());
            playlistDTO = playlistService.update(playlistDTO);
        }else {
            playlistDTO = new PlaylistDTO();
            playlistDTO.setName(genreDTO.getName() + " Top 100");
            playlistDTO.setGenreId(genreDTO.getId());
            playlistDTO.setCreationDate(new Date());
            playlistDTO = playlistService.save(playlistDTO);
        }


        List<BeatportSong> beatportSongList = new ArrayList<>();
        SongDTO songDTO;
        List<ArtistDTO> artistDTOS;
        ArtistDTO artistDTO;
        SongArtistsDTO songArtistsDTO;
        PlaylistSongsDTO playlistSongsDTO;
        JSONObject object;
        BeatportSong beatportSong;
        String songName;
        String songImage;
        List<String> artists;
        JSONArray array;
        JSONObject artistObject;
        SpotifySong spotifySong;


        for (int i = 0; i < jsonArray.length(); i++) {
            songArtistsDTO = new SongArtistsDTO();
            playlistSongsDTO = new PlaylistSongsDTO();
            songDTO = new SongDTO();
            artistDTOS = new ArrayList<>();
            object = jsonArray.getJSONObject(i);
            beatportSong = new BeatportSong();
            songName = object.get("name").toString();
            songImage = object.getJSONObject("release").getJSONObject("image").get("uri").toString();


            artists = new ArrayList<>();
            array = object.getJSONArray("artists");
            List<SongDTO> songDTOS = songService.getSongeByName(songName);
            if(songDTOS.isEmpty()){
                songDTO.setBeatportName(songName);
                songDTO.setBeatportImageUrl(songImage);
                songDTO.setStatus("ENABLE");
                songDTO = songService.save(songDTO);
            }
            else {
                songDTO = songDTOS.get(0);
                songDTO.setStatus("ENABLE");
                songDTO = songService.update(songDTO);
            }

            for (int j = 0; j < array.length(); j++) {
                artistDTO = new ArtistDTO();
                artistObject = array.getJSONObject(j);
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

            spotifySong = spotifyAPIService.searchSong(songName + " " + clearArtistText(beatportSong.getArtists().get(0)));
            if (spotifySong != null && songDTO.getSpotifyId() == null) {
                songDTO.setSpotifyName(spotifySong.getName());
                songDTO.setSpotifyId(spotifySong.getId());
                songDTO.setSpotifyImageUrl(spotifySong.getAlbum().getImages().get(0).getUrl());
                songDTO = songService.update(songDTO);
            }

            playlistSongsDTO.setSongId(songDTO.getId());
            playlistSongsDTO.setPlaylistId(playlistDTO.getId());
            playlistSongsDTO.setStatus("ENABLE");
            playlistSongsService.save(playlistSongsDTO);
        }
        return beatportSongList;
    }

    private String stringFormatter(String input) {
        String lowercase = input.toLowerCase();
        String removedCharacters = lowercase.replaceAll("[()&/]", "");
        return removedCharacters.replaceAll("\\s+", "-");
    }

    private String clearArtistText(String artist) {
        artist = artist.trim();
        artist = StringEscapeUtils.unescapeHtml4(artist);
        //(ofc) (US) (OZ) (ITA) (UK) (BR)
        String[] array = {"(ofc)", "(US)", "(UK)", "(ITA)", "(OZ)", "(BR)", "(IT)", "(UZ)", "(FR)", "(ES)", "(YU)",
                "(CA)", "(PL)", "(BE)", "(SE)", "(DE)", "(PT)", "(NO)", "(RSA)", "(SA)", "(RO)", "(Havana)", "(VE)", "(UZ)"
                , "(FL)", "(AR)", "(EON)", "(BG)", "(ZM)", "(TN)", "(NL)", "(Palestina)", "(GB)", "(Official)", "(CO)", "(Aus)"
                , "(DnB)", "(MX)", "(HU)", "(fr)", "(It)", "(SC)", "(CZ)", "(JP)", "(SWE)", "(IL)", "(AZ)", "(TN)", "(NYC)", "(Italy)"
                , "(LA)", "(AUS)", "(ARG)", "(PE)", "(GER)", "(ESP)", "(AU)", "(SL)", "(BRA)", "(MU)", "(Paris)", "(UA)", "(IND)", "(CU)", "(GR)"
                , "(Brazil)", "(ISR)", "(Psy)", "(IE)", "(LU)", "(CL)", "(UY)", "(JPN)"};
        for (String data : array) {
            if (artist.contains(data)) {
                artist = artist.replace(data, "");
                break;
            }
        }
        if (artist.contains("(") && artist.contains(")")) {
            System.out.println("ARTIST : " + artist);
        }
        return artist;
    }

}
