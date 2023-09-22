package com.example.beatporttospotify.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.beatporttospotify.model.BeatportGenre;
import com.example.beatporttospotify.model.BeatportSong;
import com.example.beatporttospotify.service.BeatportScrapperService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BeatportScrapperServiceImpl implements BeatportScrapperService {
    @Autowired
    private WebDriver webDriver;
    @Override
    public Document getHTML(String url) {
        System.out.println("getHTML: "+url);
        webDriver.get(url);
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete'"));
        String html = webDriver.getPageSource();
        Document document = Jsoup.parse(html);
        return document;
    }

    @Override
    public List<BeatportSong> getSongs(String url) {
        return null;
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
        genreList.add(beatportGenre);
        }
        System.out.println("success!");
        return genreList;
    }

    @Override
    public List<BeatportSong> getTop100(String genre) {
        Elements songs = getHTML("https://www.beatport.com/genre/"+genre+"/top-100").select("script#__NEXT_DATA__");

        String json = songs.get(0).html();
        JSONObject jsonObject = new JSONObject(json);
        String json2= jsonObject.getJSONObject("props")
                .getJSONObject("pageProps")
                .getJSONObject("dehydratedState").getJSONArray("queries").getJSONObject(0).toString();

        jsonObject = new JSONObject(json2);
        json2 = jsonObject.getJSONObject("state").getJSONObject("data").getJSONArray("results").toString();
        JSONArray jsonArray = new JSONArray(json2);
        List<BeatportSong> beatportSongList = new ArrayList<>();
        for(int i = 0; i<jsonArray.length();i++){
            JSONObject object = jsonArray.getJSONObject(i);
            BeatportSong beatportSong = new BeatportSong();
            String songName = object.get("name").toString();
            String songImage = object.getJSONObject("release").getJSONObject("image").get("uri").toString();
            List<String> artists = new ArrayList<>();
            JSONArray array = object.getJSONArray("artists");
            for(int j = 0; j<array.length();j++){
                JSONObject artistObject = array.getJSONObject(j);
                artists.add(artistObject.get("name").toString());
            }
            beatportSong.setName(songName);
            beatportSong.setUrlImage(songImage);
            beatportSong.setArtists(artists);
            beatportSongList.add(beatportSong);
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
