import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BeatportToSpotifyApiService } from '../../services/api/beatport-to-spotify-api.service';
import { DataService } from '../../services/data/data.service';
import { BeatportGenre } from 'src/app/models/beatport-genre.interface';
import { ActivatedRoute } from '@angular/router';
import { BeatportToSpotifyRequest } from 'src/app/models/beatport-to-spotify-request.interface';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  urlGenreSelected:string = '';
  playlistName:string = '';

  genres: BeatportGenre[] = [];


  constructor(
    private router: Router,
    private beatportToSpotifyApiService: BeatportToSpotifyApiService,
    private dataService: DataService,
    private route: ActivatedRoute

  ) { }

  ngOnInit(): void {
    this.getGenres();
  }

  getGenres():void{
    this.beatportToSpotifyApiService.getGenres().subscribe(data =>{
      this.genres = data;
      this.genres = this.genres.filter(genre => genre.id !== 0);
    },
    error =>{
      console.error('Genres not found')
    }
    );
  }
  create():void{
    let split =this.urlGenreSelected.split("/");
    this.router.navigate(['/detail',this.playlistName,split[0],split[1]]);
  }
}
