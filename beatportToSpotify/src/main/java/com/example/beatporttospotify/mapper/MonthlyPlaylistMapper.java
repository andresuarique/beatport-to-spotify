package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.MonthlyPlaylist;
import com.example.beatporttospotify.dto.MonthlyPlaylistDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface MonthlyPlaylistMapper {
    @Mapping(source = "genre.id", target = "genreId")
    public MonthlyPlaylistDTO monthlyPlaylistToMonthlyPlaylistDTO(MonthlyPlaylist monthlyPlaylist);
    @Mapping(source = "genreId", target = "genre.id")
    public MonthlyPlaylist monthlyPlaylistDTOToMonthlyPlaylist(MonthlyPlaylistDTO monthlyPlaylistDTO);
    public List<MonthlyPlaylistDTO> listMonthlyPlaylistToListMonthlyPlaylistDTO(List<MonthlyPlaylist> monthlyPlaylists);
    public List<MonthlyPlaylist> listMonthlyPlaylistDTOToListMonthlyPlaylist(List<MonthlyPlaylistDTO> monthlyPlaylistDTOS);
}
