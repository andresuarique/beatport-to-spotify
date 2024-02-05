package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.PlaylistSongs;
import com.example.beatporttospotify.dto.PlaylistSongsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface PlaylistSongsMapper {
    @Mapping(source = "playlist.id", target = "playlistId")
    @Mapping(source = "song.id", target = "songId")
    public PlaylistSongsDTO playlistSongsToPlaylistSongsDTO(PlaylistSongs playlistSongs);
    @Mapping(source = "playlistId", target = "playlist.id")
    @Mapping(source = "songId", target = "song.id")
    public PlaylistSongs playlistSongsDTOToPlaylistSongs(PlaylistSongsDTO playlistSongsDTO);
    public List<PlaylistSongsDTO> listPlaylistSongsToListPlaylistSongsDTO(List<PlaylistSongs> playlistSongs);
    public List<PlaylistSongs> listPlaylistSongsDTOToListPlaylistSongs(List<PlaylistSongsDTO> playlistSongsDTO);
}
