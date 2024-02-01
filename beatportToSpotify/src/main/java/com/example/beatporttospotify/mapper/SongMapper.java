package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.Song;
import com.example.beatporttospotify.domain.SongArtists;
import com.example.beatporttospotify.dto.SongDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(uses = ArtistMapper.class)
public interface SongMapper {
    @Mapping(source = "songArtists", target = "artistsDTO")
    public SongDTO songToSongDTO(Song song);
    public Song songDTOToSong(SongDTO songDTO);
    public List<SongDTO> listSongToListSongDTO(List<Song> songs);
    public List<Song> listSongDTOToListSong(List<SongDTO> songDTOs);

}
