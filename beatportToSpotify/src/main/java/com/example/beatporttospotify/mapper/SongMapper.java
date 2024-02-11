package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.Song;
import com.example.beatporttospotify.dto.SongDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = ArtistMapper.class)
public interface SongMapper {

    public SongDTO songToSongDTO(Song song);
    public Song songDTOToSong(SongDTO songDTO);
    public List<SongDTO> listSongToListSongDTO(List<Song> songs);
    public List<Song> listSongDTOToListSong(List<SongDTO> songDTOs);

}
