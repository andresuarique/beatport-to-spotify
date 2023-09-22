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
  spotifyLogin:boolean = false;
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
    const state = this.route.snapshot.paramMap.get('state')
    if(state){
      if(state=="logged"){
        this.spotifyLogin=true;
      }
    }
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
    //const url="?genreName="+split[0]+"&genreId="+split[1];

    this.spotifyLogin=!this.spotifyLogin;
    /*this.beatportToSpotifyApiService.getCallback(url).subscribe(data =>{
     window.open(data.url,"_self");
    });*/
    this.router.navigate(['/detail',this.playlistName,split[0],split[1]]);
  }
  spotifyVerify():void{
    this.spotifyLogin=!this.spotifyLogin;
    this.beatportToSpotifyApiService.getCallback().subscribe(data =>{
      window.open(data.url,"_self");
     });
  }
}
