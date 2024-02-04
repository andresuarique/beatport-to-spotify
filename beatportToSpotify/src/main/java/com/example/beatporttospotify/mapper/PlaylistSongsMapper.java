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
    public PlaylistSongsDTO playlistSongsDTOToPlaylistSongs(PlaylistSongsDTO playlistSongsDTO);
    public List<PlaylistSongsDTO> listPlaylistSongsToListPlaylistSongsDTO(List<PlaylistSongs> playlistSongs);
    public List<PlaylistSongsDTO> listPlaylistSongsDTOToListPlaylistSongs(List<PlaylistSongsDTO> playlistSongsDTO);
}
