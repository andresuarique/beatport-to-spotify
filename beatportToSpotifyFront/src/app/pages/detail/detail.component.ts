import { Component, OnInit } from '@angular/core';
import { BeatportToSpotifyApiService } from '../../services/api/beatport-to-spotify-api.service';
import { DataService } from '../../services/data/data.service';
import { ActivatedRoute } from '@angular/router';
import { BeatportToSpotifyRequest } from '../../models/beatport-to-spotify-request.interface';
import { delay } from 'rxjs/operators';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {
  request: BeatportToSpotifyRequest ={
    playlistName:'',
    genre:'',
    songs:null
  };
  image:string ='https://i.scdn.co/image/ab67616d0000b273d321650e4e1f239f314f364c';
  playlistUrl:string = '';
  ready:boolean=true;
  constructor(
    private beatportToSpotifyApiService: BeatportToSpotifyApiService,
    private dataService: DataService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    //this.createPlaylist();

  }
  createPlaylist():void{
    const playlist = this.route.snapshot.paramMap.get('playlistName');
    const name = this.route.snapshot.paramMap.get('genreName');
    const id = this.route.snapshot.paramMap.get('genreId');
    console.log("datos: "+playlist+"/"+name+"/"+id);
    if(name && id && playlist){
      this.request.playlistName=playlist;
      this.request.genre=name+"/"+id;
      this.beatportToSpotifyApiService.createPlaylist(this.request).subscribe(data =>{
        this.image=data.playlist.images[0].url
        this.playlistUrl=data.playlist.external_urls.spotify;
        this.ready=!this.ready
      });
    }
  }
}
