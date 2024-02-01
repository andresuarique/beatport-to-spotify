package com.example.beatporttospotify.dto;


import java.util.ArrayList;
import java.util.List;

public class SongDTO {
    private Long id;
    private String spotifyName;
    private String beatportName;
    private String spotifyId;
    private String beatportImageUrl;
    private String status;
    private List<ArtistDTO> artistsDTO = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotifyName() {
        return spotifyName;
    }

    public void setSpotifyName(String spotifyName) {
        this.spotifyName = spotifyName;
    }

    public String getBeatportName() {
        return beatportName;
    }

    public void setBeatportName(String beatportName) {
        this.beatportName = beatportName;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getBeatportImageUrl() {
        return beatportImageUrl;
    }

    public void setBeatportImageUrl(String beatportImageUrl) {
        this.beatportImageUrl = beatportImageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ArtistDTO> getArtistsDTO() {
        return artistsDTO;
    }

    public void setArtistsDTO(List<ArtistDTO> artistsDTO) {
        this.artistsDTO = artistsDTO;
    }
}
