package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.dto.*;
import com.example.beatporttospotify.model.spotify.SpotifyPlaylist;
import com.example.beatporttospotify.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class B2SServiceImpl implements B2SService {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistSongsService playlistSongsService;
    @Autowired
    private MonthlyPlaylistService monthlyPlaylistService;
    @Autowired
    private MonthlyPlaylistSongsService monthlyPlaylistSongsService;
    @Autowired
    private SongService songService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private SpotifyAPIService spotifyAPIService;
    @Autowired
    private BeatportScrapperService beatportScrapperService;
    @Autowired
    private SongArtistsService songArtistsService;
    private static final Logger logger = LoggerFactory.getLogger(B2SServiceImpl.class);
    @Override
    public Map<String, Object> getPlaylistByGenreCode(String genreCode) {
        Map<String, Object> response = new HashMap<>();
        try {
            PlaylistDTO playlistDTO = playlistService.getPlaylistByGenre(genreCode);
            List<PlaylistSongsDTO> listPlaylistSongsDTO = playlistSongsService.getPlaylistSongsByPlaylist(playlistDTO);
            List<SongDTO> songDTOS = new ArrayList<>();
            listPlaylistSongsDTO.forEach(playlistSongsDTO -> songDTOS.add(songService.getSongsById(playlistSongsDTO.getSongId())));
            response.put("success", true);
            response.put("playlist", playlistDTO);
            response.put("songs", songDTOS);
        }catch (Exception e){
            response.put("success", false);
            response.put("error",e.getMessage());
        }
        return response;
    }

    @Override
    public Map<String, Object> getMonthlyPlaylistByGenreCode(int year, int month, String genreCode) {
        Map<String, Object> response = new HashMap<>();
        try {
            MonthlyPlaylistDTO monthlyPlaylistDTO = monthlyPlaylistService.getMonthlyPlaylistByGenreAndMonth(year,month,genreCode);
            List<MonthlyPlaylistSongsDTO> monthlyPlaylistSongsDTOS = monthlyPlaylistSongsService.getMonthlyPlaylistSongsByMonthlyPlaylist(monthlyPlaylistDTO);
            List<SongDTO> songDTOS = new ArrayList<>();
            monthlyPlaylistSongsDTOS.forEach(monthlyPlaylistSongsDTO -> songDTOS.add(songService.getSongsById(monthlyPlaylistSongsDTO.getSongId())));
            response.put("success", true);
            response.put("playlist", monthlyPlaylistSongsDTOS);
            response.put("songs", songDTOS);
        }catch (Exception e){
            response.put("success", false);
            response.put("error",e.getMessage());
        }
        return response;
    }

    @Override
    public Map<String, Object> createPlaylist(B2SRequestDTO request,String userid, String authorizationCode) {
        Map<String, Object> response = new HashMap<>();
        try{
            List<GenreDTO> genreDTOS = genreService.getGenreByUrl(request.getGenre());
            if(genreDTOS.isEmpty()){
                response.put("success",false);
                response.put("error","genre not found");
                return response;
            }
            String genreCode = genreDTOS.get(0).getCode();
            PlaylistDTO playlistDTO = playlistService.getPlaylistByGenre(genreCode);
            if(playlistDTO == null){
                response.put("success",false);
                response.put("error","playlist not found");
                return response;
            }
            List<PlaylistSongsDTO> listPlaylistSongsDTO = playlistSongsService.getPlaylistSongsByPlaylist(playlistDTO);
            if(listPlaylistSongsDTO.isEmpty()){
                response.put("success",false);
                response.put("error","songs not found");
                return response;
            }
            List<SongDTO> songDTOS = new ArrayList<>();
            listPlaylistSongsDTO.forEach(playlistSongsDTO -> {
                SongDTO songDTO = songService.getSongsById(playlistSongsDTO.getSongId());
                songDTO.setArtists(songArtistsService.getSongsArtistsBySongId(songDTO.getId()));
                songDTOS.add(songDTO);
            });
            SpotifyPlaylist spotifyPlaylist = spotifyAPIService.createPlaylist(request.getPlaylistName(),userid,authorizationCode);
            spotifyAPIService.addSongsFromDB(songDTOS,spotifyPlaylist.getId(),authorizationCode);
            spotifyPlaylist = spotifyAPIService.getPlaylist(authorizationCode, spotifyPlaylist.getId());
            response.put("success",true);
            response.put("playlist",spotifyPlaylist);
            response.put("tracks",songDTOS);
            return response;
        }catch (Exception e){
            logger.error(e.getMessage());
            response.put("success",false);
            response.put("error",e.getMessage());
            return response;
        }

    }
    @Override
    public Map<String, Object> updatePlaylist() {
        logger.info("updatePlaylists Job {}",new Date());
        Map<String, Object> response = new HashMap<>();
        try{
            List<GenreDTO> genreDTOS = genreService.getGenres();

            genreDTOS.forEach(genreDTO -> {
                logger.info("update playlist for {} genre",genreDTO.getName());
                PlaylistDTO playlistDTO = playlistService.getPlaylistByGenre(genreDTO.getCode());
                if(playlistDTO != null){
                    playlistSongsService.disableAllSongs(playlistDTO.getId());
                }
                try {
                    beatportScrapperService.getTop100(genreDTO.getName() + "/" + genreDTO.getCode());
                }catch (Exception e){
                    logger.error("cannot get playlist for {} genre",genreDTO.getName());
                }
            });
            response.put("success",true);
        }catch (Exception e){
            logger.error(e.getMessage());
            response.put("success",false);
        }
        return response;
    }
}
