package com.example.beatporttospotify.dto;

import com.example.beatporttospotify.domain.PlaylistSongs;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaylistDTO {
    private Long id;
    private String name;
    private Date creationDate;
    private Date modificationDate;
    private List<SongDTO> songsDTO = new ArrayList<>();
}
