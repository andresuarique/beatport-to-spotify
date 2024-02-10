package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.Playlist;
import com.example.beatporttospotify.dto.PlaylistDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface PlaylistMapper {
    @Mapping(source = "genre.id", target = "genreId")
    public PlaylistDTO playlistToPlaylistDTO(Playlist playlist);
    @Mapping(source = "genreId", target = "genre.id")
    public Playlist playlistDTOToPlaylist(PlaylistDTO playlistDTO);
    public List<PlaylistDTO> listPlaylistToListPlaylistDTO(List<Playlist> playlists);
    public List<Playlist> listPlaylistDTOToListPlaylist(List<PlaylistDTO> playlistDTOs);
}
