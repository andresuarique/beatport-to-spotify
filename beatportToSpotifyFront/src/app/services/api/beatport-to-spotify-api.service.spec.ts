import { TestBed } from '@angular/core/testing';

import { BeatportToSpotifyApiService } from './beatport-to-spotify-api.service';

describe('BeatportToSpotifyApiService', () => {
  let service: BeatportToSpotifyApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeatportToSpotifyApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
