package com.example.beatporttospotify.controller;

import com.example.beatporttospotify.service.BeatportScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scrapper/beatport")
@CrossOrigin(origins = "*")
public class BeatportScrapper {
@Autowired
private BeatportScrapperService beatportScrapperService;
    @GetMapping("/genres")
    public ResponseEntity<?> getGenres(){
        return ResponseEntity.ok(beatportScrapperService.getGenres());
    }
    @GetMapping("/top-100/{genre}/{genreId}")
    public ResponseEntity<?> getTop100(@PathVariable String genre,@PathVariable String genreId){
        return ResponseEntity.ok(beatportScrapperService.getTop100(genre+"/"+genreId));
    }
}
