import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { BeatportGenre } from 'src/app/models/beatport-genre.interface';
import { BeatportToSpotifyRequest } from 'src/app/models/beatport-to-spotify-request.interface';

@Injectable({
  providedIn: 'root',
})
export class BeatportToSpotifyApiService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getCallback(): Observable<any> {
    return this.http.get<any>(this.apiUrl + '/api/spotify/callback');
  }

  getGenres(): Observable<any> {
    return this.http.get<any>(
      this.apiUrl + '/b2s/genres'
    );
  }
  createPlaylist(data: any): Observable<any> {
    return this.http.post<any>(
      this.apiUrl + '/b2s/create-playlist',
      data
    );
  }
}
