package com.example.beatporttospotify.controller;

import com.example.beatporttospotify.dto.GenreDTO;
import com.example.beatporttospotify.dto.PlaylistDTO;
import com.example.beatporttospotify.dto.PlaylistSongsDTO;
import com.example.beatporttospotify.service.B2SService;
import com.example.beatporttospotify.service.GenreService;
import com.example.beatporttospotify.service.PlaylistService;
import com.example.beatporttospotify.service.PlaylistSongsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/b2s")
@CrossOrigin(origins = "*")
public class B2SController {
    @Autowired
    private GenreService genreService;
    @Autowired
    private B2SService b2SService;

    @GetMapping("/genres")
    public ResponseEntity<?> getGenres(){
        return ResponseEntity.ok(genreService.getGenres());
    }
    @GetMapping("/playlist/{genreName}/{genreCode}")
    public ResponseEntity<?> getPlaylist(@PathVariable("genreName")String genreName,@PathVariable("genreCode")String genreCode){
        return ResponseEntity.ok(b2SService.getPlaylistByGenreCode(genreCode));
    }
}
