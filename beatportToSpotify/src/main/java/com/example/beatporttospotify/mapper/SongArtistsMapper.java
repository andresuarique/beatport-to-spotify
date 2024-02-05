package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.SongArtists;
import com.example.beatporttospotify.dto.SongArtistsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface SongArtistsMapper {
    @Mapping(source = "artist.id", target = "artistId")
    @Mapping(source = "song.id", target = "songId")
    public SongArtistsDTO songArtistToSongArtistsDTO(SongArtists songArtists);
    @Mapping(source = "artistId", target = "artist.id")
    @Mapping(source = "songId", target = "song.id")
    public SongArtists songArtistDTOToSongArtists(SongArtistsDTO songArtistsDTO);
    public List<SongArtistsDTO> listSongArtistToListSongArtistsDTO(List<SongArtists> songArtists);
    public List<SongArtists> listSongArtistDTOToListSongArtists(List<SongArtistsDTO> songArtistsDTO);
}
