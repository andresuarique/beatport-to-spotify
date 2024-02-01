package com.example.beatporttospotify.dto;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class PlaylistDTO {
    private Long id;
    private String name;
    private Date creationDate;
    private Date modificationDate;
    private List<SongDTO> songsDTO = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public List<SongDTO> getSongsDTO() {
        return songsDTO;
    }

    public void setSongsDTO(List<SongDTO> songsDTO) {
        this.songsDTO = songsDTO;
    }
}
