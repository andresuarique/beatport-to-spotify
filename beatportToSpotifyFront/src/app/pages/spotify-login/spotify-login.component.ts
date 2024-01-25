import { Component, OnInit } from '@angular/core';
import { BeatportToSpotifyApiService } from '../../services/api/beatport-to-spotify-api.service';

@Component({
  selector: 'app-spotify-login',
  templateUrl: './spotify-login.component.html',
  styleUrls: ['./spotify-login.component.scss'],
})
export class SpotifyLoginComponent implements OnInit {
  constructor(
    private beatportToSpotifyApiService: BeatportToSpotifyApiService
  ) {}

  ngOnInit(): void {}
  spotifyVerify(): void {
    this.beatportToSpotifyApiService.getCallback().subscribe((data) => {
      window.open(data.url, '_self');
    });
  }
}
