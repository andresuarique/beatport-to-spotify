package com.example.beatporttospotify.mapper;

import com.example.beatporttospotify.domain.Playlist;
import com.example.beatporttospotify.domain.PlaylistSongs;
import com.example.beatporttospotify.dto.PlaylistDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface PlaylistMapper {
    public PlaylistDTO playlistToPlaylistDTO(Playlist playlist);
    public Playlist playlistDTOToPlaylist(PlaylistDTO playlistDTO);
    public List<PlaylistDTO> listPlaylistToListPlaylistDTO(List<Playlist> playlists);
    public List<Playlist> listPlaylistDTOToListPlaylist(List<PlaylistDTO> playlistDTOs);
}
