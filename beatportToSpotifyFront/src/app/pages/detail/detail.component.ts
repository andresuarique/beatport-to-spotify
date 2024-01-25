import { Component, OnInit } from '@angular/core';
import { BeatportToSpotifyApiService } from '../../services/api/beatport-to-spotify-api.service';
import { DataService } from '../../services/data/data.service';
import { ActivatedRoute } from '@angular/router';
import { BeatportToSpotifyRequest } from '../../models/beatport-to-spotify-request.interface';
import { delay } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
})
export class DetailComponent implements OnInit {
  url: string = '../../../assets/data.json';
  tracks: any[] = [];

  request: BeatportToSpotifyRequest = {
    playlistName: '',
    genre: '',
    songs: null,
  };
  image: string = '';
  playlistUrl: string = '';
  beatportUrl: string = 'https://www.beatport.com/';
  ready: boolean = false;
  hasError: boolean = false;
  constructor(
    private beatportToSpotifyApiService: BeatportToSpotifyApiService,
    private dataService: DataService,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.createPlaylist();
  }
  createPlaylist(): void {
    const playlist = this.route.snapshot.paramMap.get('playlistName');
    const name = this.route.snapshot.paramMap.get('genreName');
    const id = this.route.snapshot.paramMap.get('genreId');
    console.log('datos: ' + playlist + '/' + name + '/' + id);
    if (name && id && playlist) {
      this.request.playlistName = playlist;
      this.request.genre = name + '/' + id;

      this.beatportToSpotifyApiService.createPlaylist(this.request).subscribe(
        (data) => {
          //this.http.get<any>(this.url).subscribe(data =>{
          this.image = data.playlist.images[0].url;
          this.playlistUrl = data.playlist.external_urls.spotify;
          this.tracks = data.tracks.items;
          this.ready = true;
          this.hasError = false;

          if (name == 'general') {
            this.beatportUrl += 'top-100';
          } else {
            this.beatportUrl += 'genre/' + name + '/' + id + '/top-100';
          }
        },
        (error) => {
          this.ready = true;
          this.hasError = true;
        }
      );
    }
  }
}
