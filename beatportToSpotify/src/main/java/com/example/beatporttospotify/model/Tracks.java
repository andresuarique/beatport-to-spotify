package com.example.beatporttospotify.model;

import lombok.Data;

import java.util.List;

@Data
public class Tracks {
    private List<SpotifySong> items;
}
