package com.example.beatporttospotify.domain;

import javax.persistence.*;

@Entity
@Table(name = "monthly_playlist_songs")
public class MonthlyPlaylistSongs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monthly_playlist_id")
    private MonthlyPlaylist Monthlyplaylist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;

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

    public MonthlyPlaylist getMonthlyplaylist() {
        return Monthlyplaylist;
    }

    public void setMonthlyplaylist(MonthlyPlaylist monthlyplaylist) {
        Monthlyplaylist = monthlyplaylist;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
