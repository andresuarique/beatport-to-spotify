package com.example.beatporttospotify.model.spotify;


import java.util.List;

public class Tracks {
    private List<SpotifySong> items;

    public List<SpotifySong> getItems() {
        return items;
    }

    public void setItems(List<SpotifySong> items) {
        this.items = items;
    }
}
