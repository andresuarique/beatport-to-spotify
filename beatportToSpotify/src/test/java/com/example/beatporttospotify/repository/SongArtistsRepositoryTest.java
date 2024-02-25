package com.example.beatporttospotify.repository;
import com.example.beatporttospotify.domain.Artist;
import com.example.beatporttospotify.domain.Song;
import com.example.beatporttospotify.domain.SongArtists;
import com.example.beatporttospotify.dto.SongDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SongArtistsRepositoryTest {
    @Autowired
    private SongArtistsRepository songArtistsRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private ArtistRepository artistRepository;

    private Song createSong(String beatportName, String spotifyName,
                            String spotifyId, String beatportImageUrl,
                            String spotifyImageUrl){
        Song song = new Song();
        song.setBeatportName(beatportName);
        song.setSpotifyName(spotifyName);
        song.setSpotifyId(spotifyId);
        song.setBeatportImageUrl(beatportImageUrl);
        song.setSpotifyImageUrl(spotifyImageUrl);
        song.setStatus(SongDTO.ENABLE);
        return song;
    }
    private Artist createArtist(String spotifyName, String beatportName){
        Artist artist = new Artist();
        artist.setSpotifyName(spotifyName);
        artist.setBeatportName(beatportName);
        return artist;
    }
    private SongArtists createSongArtist(Song song, Artist artist){
        SongArtists songArtists = new SongArtists();
        songArtists.setSong(song);
        songArtists.setArtist(artist);
        return songArtists;
    }

    @Test
    public void saveSongArtistTest(){
        //given
        Song song = createSong("Weak","Weak",
                "0001","imageB.png","imageS.png");
        song = songRepository.save(song);

        Artist artist = createArtist("Vintage Culture", "Vintage Culture");
        artist = artistRepository.save(artist);

        SongArtists songArtists = createSongArtist(song,artist);

        //when
        SongArtists savedSongArtist = songArtistsRepository.save(songArtists);

        //then
        assertThat(savedSongArtist).isNotNull();
        assertThat(savedSongArtist.getId()).isGreaterThan(0);
    }

    @Test
    public void updateSongArtistTest(){
        //given
        Song song = createSong("Weak","Weak",
                "0001","imageB.png","imageS.png");
        song = songRepository.save(song);

        Artist artist = createArtist("Vintage Culture", "Vintage Culture");
        artist = artistRepository.save(artist);

        SongArtists songArtists = createSongArtist(song,artist);
        SongArtists savedSongArtist = songArtistsRepository.save(songArtists);

        artist.setSpotifyName("VINTAGE CULTURE");

        savedSongArtist.setArtist(artist);

        //when
        savedSongArtist = songArtistsRepository.save(songArtists);

        //then
        assertThat(savedSongArtist.getArtist().getSpotifyName()).isNotEqualTo("Vintage Culture");
        assertThat(savedSongArtist.getArtist().getSpotifyName()).isEqualTo("VINTAGE CULTURE");
    }

    @Test
    public void findAllSongArtistsTest(){
        //given
        Song song1 = createSong("Weak","Weak",
                "0001","imageB.png","imageS.png");
        song1 = songRepository.save(song1);

        Artist artist1 = createArtist("Vintage Culture", "Vintage Culture");
        artist1 = artistRepository.save(artist1);

        SongArtists songArtists = createSongArtist(song1,artist1);
        SongArtists savedSongArtist = songArtistsRepository.save(songArtists);

        Song song2 = createSong("Cloud","Cloud",
                "0002","image001B.png","image001S.png");
        song2 = songRepository.save(song2);

        Artist artist2 = createArtist("Gruuve", "Gruuve");
        artist2 = artistRepository.save(artist2);

        SongArtists songArtists2 = createSongArtist(song2,artist2);
        SongArtists savedSongArtist2 = songArtistsRepository.save(songArtists2);

        //when
        List<SongArtists> songArtistsList = songArtistsRepository.findAll();

        //then
        assertThat(songArtistsList).hasSize(2);
    }

    @Test
    public void findAllSongArtistsBySongAndArtistTest(){
        //given
        Song song1 = createSong("Weak","Weak",
                "0001","imageB.png","imageS.png");
        song1 = songRepository.save(song1);

        Artist artist1 = createArtist("Vintage Culture", "Vintage Culture");
        artist1 = artistRepository.save(artist1);

        SongArtists songArtists = createSongArtist(song1,artist1);
        SongArtists savedSongArtist = songArtistsRepository.save(songArtists);

        Song song2 = createSong("Cloud","Cloud",
                "0002","image001B.png","image001S.png");
        song2 = songRepository.save(song2);

        Artist artist2 = createArtist("Gruuve", "Gruuve");
        artist2 = artistRepository.save(artist2);

        SongArtists songArtists2 = createSongArtist(song2,artist2);
        SongArtists savedSongArtist2 = songArtistsRepository.save(songArtists);

        //when
        SongArtists songAndArtist = songArtistsRepository.findBySongAndArtist(song1,artist1);

        //then
        assertThat(songAndArtist.getSong().getBeatportName()).isEqualTo("Weak");
    }

    @Test
    public void findAllSongArtistsBySongTest(){
        //given
        Song song1 = createSong("Weak","Weak",
                "0001","imageB.png","imageS.png");
        song1 = songRepository.save(song1);

        Artist artist1 = createArtist("Vintage Culture", "Vintage Culture");
        artist1 = artistRepository.save(artist1);

        SongArtists songArtists = createSongArtist(song1,artist1);
        SongArtists savedSongArtist = songArtistsRepository.save(songArtists);

        Song song2 = createSong("Cloud","Cloud",
                "0002","image001B.png","image001S.png");
        song2 = songRepository.save(song2);

        Artist artist2 = createArtist("Gruuve", "Gruuve");
        artist2 = artistRepository.save(artist2);

        SongArtists songArtists2 = createSongArtist(song2,artist2);
        SongArtists savedSongArtist2 = songArtistsRepository.save(songArtists);

        //when
        List<SongArtists> songArtistsList = songArtistsRepository.findBySong(song1);

        //then
        assertThat(songArtistsList).hasSize(1);
        assertThat(songArtistsList.get(0).getSong().getBeatportName()).isEqualTo("Weak");
    }
}
