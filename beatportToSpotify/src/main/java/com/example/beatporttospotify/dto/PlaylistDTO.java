package com.example.beatporttospotify.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PlaylistDTO {
    private Long id;
    private String name;
    private Date creationDate;
    private Date modificationDate;
    private List<SongDTO> songsDTO = new ArrayList<>();
}
