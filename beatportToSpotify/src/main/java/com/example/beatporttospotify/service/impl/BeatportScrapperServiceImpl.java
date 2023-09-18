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
        //System.out.println(document.html());
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
            System.out.println(object);
        BeatportGenre beatportGenre = new BeatportGenre();
        Long id =0L;
            System.out.println(object.get("id"));
            try {
                id = Long.parseLong(object.get("id").toString());
            }catch (Exception e){

            }
        beatportGenre.setId(id);
        beatportGenre.setName(object.get("name").toString());
            System.out.println(beatportGenre.getId());
            System.out.println(beatportGenre.getName());
        }
        return null;
    }
}
