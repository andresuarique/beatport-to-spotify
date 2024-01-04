import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './pages/home/home.component';
import { DetailComponent } from './pages/detail/detail.component';
import { SpotifyLoginComponent } from './pages/spotify-login/spotify-login.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path:'spotify-login',
    component:SpotifyLoginComponent
  },
  {
    path:'home',
    component:HomeComponent
  },
  {
    path:'detail/:playlistName/:genreName/:genreId',
    component:DetailComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
