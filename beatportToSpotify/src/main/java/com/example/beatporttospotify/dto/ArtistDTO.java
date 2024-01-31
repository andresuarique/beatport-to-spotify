package com.example.beatporttospotify.dto;

import lombok.Data;

@Data
public class ArtistDTO {
    private Long id;
    private String spotifyName;
    private String beatportName;
}
