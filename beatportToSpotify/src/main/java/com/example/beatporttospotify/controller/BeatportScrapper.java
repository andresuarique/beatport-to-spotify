package com.example.beatporttospotify.controller;

import com.example.beatporttospotify.service.BeatportScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/scrapper/beatport")
@CrossOrigin(origins = "*")
public class BeatportScrapper {
@Autowired
private BeatportScrapperService beatportScrapperService;
    @GetMapping("/genres")
    public ResponseEntity<?> getGenres(){
        Map<String,Object> response = new HashMap<>();
        try{
            response.put("genres",beatportScrapperService.getGenres());
            response.put("success",true);
        }catch (Exception e){
            response.put("error",e.getMessage());
            response.put("success",true);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/top-100/{genre}/{genreId}")
    public ResponseEntity<?> getTop100(@PathVariable String genre,@PathVariable String genreId){
        Map<String,Object> response= new HashMap<>();
        try{
            response.put("songs",beatportScrapperService.getTop100(genre+"/"+genreId));
            response.put("success",true);
        }catch (Exception e){
            response.put("error",e.getMessage());
            response.put("success",true);
        }
        return ResponseEntity.ok(response);
    }
}
