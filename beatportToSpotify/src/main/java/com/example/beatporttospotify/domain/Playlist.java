package com.example.beatporttospotify.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "playlist")
@Data
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "modification_date")
    private Date modificationDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "playlist")
    private List<PlaylistSongs> playlistSongs = new ArrayList<>();
}
