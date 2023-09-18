package com.example.beatporttospotify.controller;

import com.example.beatporttospotify.service.BeatportScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrapper/beatport")
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
