package com.example.beatporttospotify.dto;


public class MonthlyPlaylistSongsDTO {
    public static final String ENABLE = "ENABLE";
    public static final String DISABLE = "DISABLE";
    private Long id;
    private String status;
    private Long monthlyPlaylistId;
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

    public Long getMonthlyPlaylistId() {
        return monthlyPlaylistId;
    }

    public void setMonthlyPlaylistId(Long monthlyPlaylistId) {
        this.monthlyPlaylistId = monthlyPlaylistId;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }
}
