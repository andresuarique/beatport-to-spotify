package com.example.beatporttospotify.repository;
import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.domain.MonthlyPlaylist;
import com.example.beatporttospotify.domain.Playlist;
import com.example.beatporttospotify.dto.GenreDTO;
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
public class PlaylistRepositoryTest {

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private GenreRepository genreRepository;

    public Playlist createPlaylist(String playlistName, Genre genre){
        Playlist playlist = new Playlist();
        playlist.setName(playlistName);
        playlist.setCreationDate(new Date());
        playlist.setModificationDate(null);
        playlist.setGenre(genre);
        return playlist;
    }

    private Genre createGenre(String name, String code){
        Genre genre = new Genre();
        genre.setName(name);
        genre.setCode(code);
        genre.setStatus(GenreDTO.ENABLE);
        return genre;
    }

    @Test
    public void savePlaylistTeam(){
        //given
        Genre genre = createGenre("techno","1");
        genre = genreRepository.save(genre);
        Playlist playlist = createPlaylist("techno playlist",genre);

        //when
        Playlist savedPlaylist = playlistRepository.save(playlist);

        //then
        assertThat(savedPlaylist).isNotNull();
        assertThat(savedPlaylist.getId()).isGreaterThan(0);
    }

    @Test
    public void updatePlaylistTeam(){
        //given
        Genre genre = createGenre("techno","1");
        genre = genreRepository.save(genre);
        Playlist playlist = createPlaylist("techno playlist",genre);
        Playlist savedPlaylist = playlistRepository.save(playlist);

        //when
        savedPlaylist.setName("Techno Top 100");
        savedPlaylist = playlistRepository.save(savedPlaylist);

        //then
        assertThat(savedPlaylist.getName()).isNotEqualTo("techno playlist");
        assertThat(savedPlaylist.getName()).isEqualTo("Techno Top 100");
    }

    @Test
    public void findAllPlaylistsTest(){
        //given
        Genre genre = createGenre("techno","1");
        genre = genreRepository.save(genre);
        Playlist playlist1 = createPlaylist("techno playlist",genre);
        Playlist savedPlaylist1 = playlistRepository.save(playlist1);
        Playlist playlist2 = createPlaylist("techno top 100",genre);
        Playlist savedPlaylist2 = playlistRepository.save(playlist2);

        //when
        List<Playlist> playlistList = playlistRepository.findAll();

        //then
        assertThat(playlistList).hasSize(2);
    }

    @Test
    public void findAllPlaylistsByGenreTest(){
        //given
        Genre genre = createGenre("techno","1");
        genre = genreRepository.save(genre);
        Playlist playlist1 = createPlaylist("techno playlist",genre);
        Playlist savedPlaylist1 = playlistRepository.save(playlist1);

        //when
        Playlist playlist = playlistRepository.findByGenre(genre);

        //then
        assertThat(playlist.getName()).isEqualTo("techno playlist");
    }
}
