package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.MonthlyPlaylistSongs;
import com.example.beatporttospotify.dto.MonthlyPlaylistSongsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface MonthlyPlaylistSongsMapper {
    @Mapping(source = "monthlyPlaylist.id", target = "monthlyPlaylistId")
    @Mapping(source = "song.id", target = "songId")
    public MonthlyPlaylistSongsDTO monthlyPlaylistSongsToMonthlyPlaylistSongsDTO(MonthlyPlaylistSongs monthlyPlaylistSongs);
    @Mapping(source = "monthlyPlaylistId", target = "monthlyPlaylist.id")
    @Mapping(source = "songId", target = "song.id")
    public MonthlyPlaylistSongs monthlyPlaylistSongsDTOToMonthlyPlaylistSongs(MonthlyPlaylistSongsDTO monthlyPlaylistSongsDTO);
    public List<MonthlyPlaylistSongsDTO> listMonthlyPlaylistSongsToListMonthlyPlaylistSongsDTO(List<MonthlyPlaylistSongs> monthlyPlaylistSongs);
    public List<MonthlyPlaylistSongs> listMonthlyPlaylistSongsDTOToListMonthlyPlaylistSongs(List<MonthlyPlaylistSongsDTO> monthlyPlaylistSongsDTOS);
}
