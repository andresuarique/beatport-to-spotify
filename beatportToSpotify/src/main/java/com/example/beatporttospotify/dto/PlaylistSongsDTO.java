package com.example.beatporttospotify.dto;


public class PlaylistSongsDTO {
    public static final String ENABLE = "ENABLE";
    public static final String DISABLE = "DISABLE";
    private Long id;
    private String status;
    private Long playlistId;

    private Long songId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

}
