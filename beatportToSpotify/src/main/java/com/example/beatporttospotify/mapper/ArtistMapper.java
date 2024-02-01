package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.Artist;
import com.example.beatporttospotify.dto.ArtistDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ArtistMapper {
    public ArtistDTO artistToArtistDTO(Artist artist);
    public Artist artistDTOToArtist(ArtistDTO artistDTO);
    public List<ArtistDTO> listArtistToListArtistDTO(List<Artist> artists);
    public List<Artist> ListArtistDTOToListArtist(List<ArtistDTO> artistDTOs);
}
