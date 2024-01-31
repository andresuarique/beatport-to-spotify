package com.example.beatporttospotify.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SongDTO {
    private Long id;
    private String spotifyName;
    private String beatportName;
    private String spotifyId;
    private String beatportImageUrl;
    private String status;
    private List<ArtistDTO> artistsDTO = new ArrayList<>();
}
