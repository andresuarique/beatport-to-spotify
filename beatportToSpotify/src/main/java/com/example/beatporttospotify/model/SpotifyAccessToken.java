package com.example.beatporttospotify.model;

import lombok.Data;

import java.time.Instant;

@Data
public class SpotifyAccessToken {
    private String access_token;
    private String token_type;
    private Long expires_in;
    private String scope;
    private String refresh_token;
    private Instant token_creation_time;
}
