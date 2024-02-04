package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.SongArtists;
import com.example.beatporttospotify.dto.SongArtistsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SongArtistsMapper {
    public SongArtistsDTO songArtistToSongArtistsDTO(SongArtists songArtists);
    public SongArtists songArtistDTOToSongArtists(SongArtistsDTO songArtistsDTO);
    public List<SongArtistsDTO> listSongArtistToListSongArtistsDTO(List<SongArtists> songArtists);
    public List<SongArtists> listSongArtistDTOToListSongArtists(List<SongArtistsDTO> songArtistsDTO);
}
