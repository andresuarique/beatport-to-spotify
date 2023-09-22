import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './pages/home/home.component';
import { DetailComponent } from './pages/detail/detail.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'home/1',
    pathMatch: 'full'
  },
  {
    path:'home/:state',
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
