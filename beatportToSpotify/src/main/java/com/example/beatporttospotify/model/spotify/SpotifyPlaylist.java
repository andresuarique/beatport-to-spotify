package com.example.beatporttospotify.model.spotify;


import java.util.List;
import java.util.Map;

public class SpotifyPlaylist {
    private String id;
    private String name;
    private Map<String,String> external_urls;
    private List<SpotifyImages> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getExternal_urls() {
        return external_urls;
    }

    public void setExternal_urls(Map<String, String> external_urls) {
        this.external_urls = external_urls;
    }

    public List<SpotifyImages> getImages() {
        return images;
    }

    public void setImages(List<SpotifyImages> images) {
        this.images = images;
    }
}
