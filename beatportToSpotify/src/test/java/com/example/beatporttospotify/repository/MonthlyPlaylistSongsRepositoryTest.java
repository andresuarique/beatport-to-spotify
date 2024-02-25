package com.example.beatporttospotify.repository;
import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.domain.MonthlyPlaylist;
import com.example.beatporttospotify.domain.MonthlyPlaylistSongs;
import com.example.beatporttospotify.domain.Song;
import com.example.beatporttospotify.dto.GenreDTO;
import com.example.beatporttospotify.dto.MonthlyPlaylistDTO;
import com.example.beatporttospotify.dto.MonthlyPlaylistSongsDTO;
import com.example.beatporttospotify.dto.SongDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MonthlyPlaylistSongsRepositoryTest {

    @Autowired
    private MonthlyPlaylistSongsRepository monthlyPlaylistSongsRepository;
    @Autowired
    private MonthlyPlaylistRepository monthlyPlaylistRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private GenreRepository genreRepository;

    private MonthlyPlaylistSongs createMonthlyPlaylistSongs(MonthlyPlaylist monthlyPlaylist, Song song){
        MonthlyPlaylistSongs monthlyPlaylistSongs = new MonthlyPlaylistSongs();
        monthlyPlaylistSongs.setSong(song);
        monthlyPlaylistSongs.setMonthlyPlaylist(monthlyPlaylist);
        monthlyPlaylistSongs.setStatus(MonthlyPlaylistSongsDTO.ENABLE);
        return monthlyPlaylistSongs;
    }

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

    private MonthlyPlaylist createMonthlyPlaylist(String playlistName){
        Genre genre = new Genre();
        genre.setCode("1");
        genre.setName("techno");
        genre.setUrl("techno/1");
        genre.setStatus(GenreDTO.ENABLE);
        genre = genreRepository.save(genre);

        MonthlyPlaylist monthlyPlaylist = new MonthlyPlaylist();
        monthlyPlaylist.setName(playlistName);
        monthlyPlaylist.setCreationDate(new Date());
        monthlyPlaylist.setModificationDate(null);
        monthlyPlaylist.setGenre(genre);
        return monthlyPlaylist;
    }

    @Test
    public void saveMonthlyPlaylistSongsTest(){
        //given
        MonthlyPlaylist monthlyPlaylist = createMonthlyPlaylist("techno playlist");
        monthlyPlaylist = monthlyPlaylistRepository.save(monthlyPlaylist);

        Song song = createSong("Weak","Weak","001","image01.png","image01.png");
        song = songRepository.save(song);

        MonthlyPlaylistSongs monthlyPlaylistSongs = createMonthlyPlaylistSongs(monthlyPlaylist,song);

        //when
        monthlyPlaylistSongs = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs);

        //then
        assertThat(monthlyPlaylistSongs).isNotNull();
        assertThat(monthlyPlaylistSongs.getId()).isGreaterThan(0);
    }

    @Test
    public void updateMonthlyPlaylistSongsTest(){
        //given
        MonthlyPlaylist monthlyPlaylist = createMonthlyPlaylist("techno playlist");
        monthlyPlaylist = monthlyPlaylistRepository.save(monthlyPlaylist);

        Song song = createSong("Weak","Weak","001","image01.png","image01.png");
        song = songRepository.save(song);

        MonthlyPlaylistSongs monthlyPlaylistSongs = createMonthlyPlaylistSongs(monthlyPlaylist,song);
        monthlyPlaylistSongs = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs);

        //when
        monthlyPlaylistSongs.setStatus(MonthlyPlaylistSongsDTO.DISABLE);
        monthlyPlaylistSongs = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs);

        //then
        assertThat(monthlyPlaylistSongs.getStatus()).isNotEqualTo(MonthlyPlaylistSongsDTO.ENABLE);
        assertThat(monthlyPlaylistSongs.getStatus()).isEqualTo(MonthlyPlaylistSongsDTO.DISABLE);
    }

    @Test
    public void findAllMonthlyPlaylistlistSongsTest(){
        //given
        MonthlyPlaylist monthlyPlaylist = createMonthlyPlaylist("techno playlist");
        monthlyPlaylist = monthlyPlaylistRepository.save(monthlyPlaylist);

        Song song1 = createSong("Weak","Weak","001","image01.png","image01.png");
        song1 = songRepository.save(song1);

        Song song2 = createSong("Cloud","Cloud","002","image02.png","image02.png");
        song2 = songRepository.save(song2);

        MonthlyPlaylistSongs monthlyPlaylistSongs1 = createMonthlyPlaylistSongs(monthlyPlaylist,song1);
        MonthlyPlaylistSongs monthlyPlaylistSongs2 = createMonthlyPlaylistSongs(monthlyPlaylist,song2);

        monthlyPlaylistSongs1 = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs1);
        monthlyPlaylistSongs2 = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs2);

        //when
        List<MonthlyPlaylistSongs> monthlyPlaylistSongsList = monthlyPlaylistSongsRepository.findAll();

        //then
        assertThat(monthlyPlaylistSongsList).hasSize(2);
    }

    @Test
    public void findAllMonthlyPlaylistlistSongsByMonthlyPlaylistTest(){
        //given
        MonthlyPlaylist monthlyPlaylist = createMonthlyPlaylist("techno playlist");
        monthlyPlaylist = monthlyPlaylistRepository.save(monthlyPlaylist);

        Song song1 = createSong("Weak","Weak","001","image01.png","image01.png");
        song1 = songRepository.save(song1);

        Song song2 = createSong("Cloud","Cloud","002","image02.png","image02.png");
        song2 = songRepository.save(song2);

        MonthlyPlaylistSongs monthlyPlaylistSongs1 = createMonthlyPlaylistSongs(monthlyPlaylist,song1);
        MonthlyPlaylistSongs monthlyPlaylistSongs2 = createMonthlyPlaylistSongs(monthlyPlaylist,song2);

        monthlyPlaylistSongs1 = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs1);
        monthlyPlaylistSongs2 = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs2);

        //when
        List<MonthlyPlaylistSongs> monthlyPlaylistSongsList = monthlyPlaylistSongsRepository.findByMonthlyPlaylist(monthlyPlaylist);

        //then
        assertThat(monthlyPlaylistSongsList).hasSize(2);
    }

    @Test
    public void findAllMonthlyPlaylistlistSongsBySongAndMonthlyPlaylistTest(){
        //given
        MonthlyPlaylist monthlyPlaylist = createMonthlyPlaylist("techno playlist");
        monthlyPlaylist = monthlyPlaylistRepository.save(monthlyPlaylist);

        Song song1 = createSong("Weak","Weak","001","image01.png","image01.png");
        song1 = songRepository.save(song1);

        Song song2 = createSong("Cloud","Cloud","002","image02.png","image02.png");
        song2 = songRepository.save(song2);

        MonthlyPlaylistSongs monthlyPlaylistSongs1 = createMonthlyPlaylistSongs(monthlyPlaylist,song1);
        MonthlyPlaylistSongs monthlyPlaylistSongs2 = createMonthlyPlaylistSongs(monthlyPlaylist,song2);

        monthlyPlaylistSongs1 = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs1);
        monthlyPlaylistSongs2 = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs2);

        //when
        monthlyPlaylistSongs1 = monthlyPlaylistSongsRepository.findBySongAndMonthlyPlaylist(song1,monthlyPlaylist);
        monthlyPlaylistSongs2 = monthlyPlaylistSongsRepository.findBySongAndMonthlyPlaylist(song2,monthlyPlaylist);

        //then
        assertThat(monthlyPlaylistSongs1.getSong().getBeatportName()).isEqualTo("Weak");
        assertThat(monthlyPlaylistSongs2.getSong().getBeatportName()).isEqualTo("Cloud");
    }


    @Test
    public void disableAllSongsTest(){
        //given
        MonthlyPlaylist monthlyPlaylist = createMonthlyPlaylist("techno playlist");
        monthlyPlaylist = monthlyPlaylistRepository.save(monthlyPlaylist);

        Song song1 = createSong("Weak","Weak","001","image01.png","image01.png");
        song1 = songRepository.save(song1);

        Song song2 = createSong("Cloud","Cloud","002","image02.png","image02.png");
        song2 = songRepository.save(song2);

        MonthlyPlaylistSongs monthlyPlaylistSongs1 = createMonthlyPlaylistSongs(monthlyPlaylist,song1);
        MonthlyPlaylistSongs monthlyPlaylistSongs2 = createMonthlyPlaylistSongs(monthlyPlaylist,song2);

        monthlyPlaylistSongs1 = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs1);
        monthlyPlaylistSongs2 = monthlyPlaylistSongsRepository.save(monthlyPlaylistSongs2);

        //when
        monthlyPlaylistSongsRepository.disableAllSongs(monthlyPlaylistSongs1.getMonthlyPlaylist().getId());
        List<MonthlyPlaylistSongs> monthlyPlaylistSongsList = monthlyPlaylistSongsRepository.findAll();

        //then
        assertThat(monthlyPlaylistSongsList.get(0).getStatus()).isEqualTo(MonthlyPlaylistSongsDTO.DISABLE);
        assertThat(monthlyPlaylistSongsList.get(1).getStatus()).isEqualTo(MonthlyPlaylistSongsDTO.DISABLE);
    }
}
