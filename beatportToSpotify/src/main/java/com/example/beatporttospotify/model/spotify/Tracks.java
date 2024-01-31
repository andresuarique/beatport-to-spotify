package com.example.beatporttospotify.model.spotify;

import lombok.Data;

import java.util.List;

@Data
public class Tracks {
    private List<SpotifySong> items;
}
