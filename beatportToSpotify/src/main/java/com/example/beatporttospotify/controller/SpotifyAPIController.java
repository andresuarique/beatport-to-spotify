package com.example.beatporttospotify.controller;

import com.example.beatporttospotify.model.scraper.BeatportToSpotifyRequest;
import com.example.beatporttospotify.model.spotify.SpotifyAccessToken;
import com.example.beatporttospotify.model.spotify.SpotifyUser;
import com.example.beatporttospotify.service.SpotifyAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/api/spotify")
@CrossOrigin(origins = "*")
public class SpotifyAPIController {
}
