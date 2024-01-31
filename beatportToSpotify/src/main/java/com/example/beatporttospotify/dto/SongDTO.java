package com.example.beatporttospotify.dto;

import com.example.beatporttospotify.domain.Artist;
import com.example.beatporttospotify.domain.PlaylistSongs;
import com.example.beatporttospotify.domain.SongArtists;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
}
