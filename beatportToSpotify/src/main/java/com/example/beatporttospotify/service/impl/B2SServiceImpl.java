package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.dto.*;
import com.example.beatporttospotify.model.spotify.SpotifyPlaylist;
import com.example.beatporttospotify.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class B2SServiceImpl implements B2SService {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistSongsService playlistSongsService;
    @Autowired
    private SongService songService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private SpotifyAPIService spotifyAPIService;
    @Autowired
    private BeatportScrapperService beatportScrapperService;
    @Override
    public Map<String, Object> getPlaylistByGenreCode(String genreCode) {
        Map<String, Object> response = new HashMap<>();
        PlaylistDTO playlistDTO = playlistService.getPlaylistByGenre(genreCode);
        List<PlaylistSongsDTO> listPlaylistSongsDTO = playlistSongsService.getPlaylistSongsByPlaylist(playlistDTO);
        List<SongDTO> songDTOS = new ArrayList<>();
        listPlaylistSongsDTO.forEach(playlistSongsDTO -> {
            songDTOS.add(songService.getSongsById(playlistSongsDTO.getSongId()));
        });
        response.put("playlist",playlistDTO);
        response.put("songs",songDTOS);
        return response;
    }

    @Override
    public Map<String, Object> createPlaylist(B2SRequestDTO request,String userid, String authorizationCode) {
        Map<String, Object> response = new HashMap<>();
        try{
            List<GenreDTO> genreDTOS = genreService.getGenreByName(request.getGenre());
            if(genreDTOS.isEmpty()){
               return null;
            }
            String genreCode = genreDTOS.get(0).getCode();
            PlaylistDTO playlistDTO = playlistService.getPlaylistByGenre(genreCode);
            if(playlistDTO == null){
                return null;
            }
            List<PlaylistSongsDTO> listPlaylistSongsDTO = playlistSongsService.getPlaylistSongsByPlaylist(playlistDTO);
            if(listPlaylistSongsDTO.isEmpty()){
                return null;
            }
            List<SongDTO> songDTOS = new ArrayList<>();
            listPlaylistSongsDTO.forEach(playlistSongsDTO -> {
                songDTOS.add(songService.getSongsById(playlistSongsDTO.getSongId()));
            });
            SpotifyPlaylist spotifyPlaylist = spotifyAPIService.createPlaylist(request.getPlaylistName(),userid,authorizationCode);
            String data = spotifyAPIService.addSongsFromDB(songDTOS,spotifyPlaylist.getId(),authorizationCode);
            response.put("playlist",spotifyPlaylist);
            response.put("tracks",songDTOS);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    @Override
    public Map<String, Object> updatePlaylist() {
        Map<String, Object> response = new HashMap<>();
        try{
            List<GenreDTO> genreDTOS = genreService.getGenres();

            genreDTOS.forEach(genreDTO -> {
                System.out.println("GENRE: "+genreDTO.getName());
                PlaylistDTO playlistDTO = playlistService.getPlaylistByGenre(genreDTO.getCode());
                if(playlistDTO != null){
                    playlistSongsService.disableAllSongs(playlistDTO.getId());
                }
                System.out.println(genreDTO.getName()+"/"+genreDTO.getCode());
                try {
                    beatportScrapperService.getTop100(genreDTO.getName() + "/" + genreDTO.getCode());
                }catch (Exception e){
                    System.out.println("NO SE PUDO OBTENER PLAYLIST PARA "+genreDTO.getName());
                }
            });
            response.put("success",true);
        }catch (Exception e){
            e.printStackTrace();
            response.put("success",false);
        }
        return response;
    }
}
