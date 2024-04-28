package com.example.beatporttospotify.repository;
import com.example.beatporttospotify.domain.*;
import com.example.beatporttospotify.dto.GenreDTO;
import com.example.beatporttospotify.dto.PlaylistSongsDTO;
import com.example.beatporttospotify.dto.SongDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class PlaylistSongsRepositoryTest {
    @Autowired
    private PlaylistSongsRepository playlistSongsRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private GenreRepository genreRepository;

    private PlaylistSongs createPlaylistSongs(Playlist playlist, Song song){
        PlaylistSongs playlistSongs = new PlaylistSongs();
        playlistSongs.setSong(song);
        playlistSongs.setPlaylist(playlist);
        playlistSongs.setStatus(PlaylistSongsDTO.ENABLE);
        return playlistSongs;
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

    private Playlist createPlaylist(String playlistName){
        Genre genre = new Genre();
        genre.setCode("1");
        genre.setName("techno");
        genre.setUrl("techno/1");
        genre.setStatus(GenreDTO.ENABLE);
        genre = genreRepository.save(genre);

        Playlist playlist = new Playlist();
        playlist.setName(playlistName);
        playlist.setCreationDate(new Date());
        playlist.setModificationDate(null);
        playlist.setGenre(genre);
        return playlist;
    }

    @Test
    public void savePlaylistSongsTest(){
        //given
        Playlist playlist = createPlaylist("techno playlist");
        playlist = playlistRepository.save(playlist);

        Song song = createSong("Weak","Weak","001","image01.png","image01.png");
        song = songRepository.save(song);

        PlaylistSongs playlistSongs = createPlaylistSongs(playlist,song);

        //when
        playlistSongs = playlistSongsRepository.save(playlistSongs);

        //then
        assertThat(playlistSongs).isNotNull();
        assertThat(playlistSongs.getId()).isGreaterThan(0);
    }

    @Test
    public void updatePlaylistSongsTest(){
        //given
        Playlist playlist = createPlaylist("techno playlist");
        playlist = playlistRepository.save(playlist);

        Song song = createSong("Weak","Weak","001","image01.png","image01.png");
        song = songRepository.save(song);

        PlaylistSongs playlistSongs = createPlaylistSongs(playlist,song);
        playlistSongs = playlistSongsRepository.save(playlistSongs);

        //when
        playlistSongs.setStatus(PlaylistSongsDTO.DISABLE);
        playlistSongs = playlistSongsRepository.save(playlistSongs);

        //then
        assertThat(playlistSongs.getStatus()).isNotEqualTo(PlaylistSongsDTO.ENABLE);
        assertThat(playlistSongs.getStatus()).isEqualTo(PlaylistSongsDTO.DISABLE);
    }

    @Test
    public void findAllPlaylistlistSongsTest(){
        //given
        Playlist playlist = createPlaylist("techno playlist");
        playlist = playlistRepository.save(playlist);

        Song song1 = createSong("Weak","Weak","001","image01.png","image01.png");
        song1 = songRepository.save(song1);

        Song song2 = createSong("Cloud","Cloud","002","image02.png","image02.png");
        song2 = songRepository.save(song2);

        PlaylistSongs playlistSongs1 = createPlaylistSongs(playlist,song1);
       PlaylistSongs playlistSongs2 = createPlaylistSongs(playlist,song2);

        playlistSongs1 = playlistSongsRepository.save(playlistSongs1);
        playlistSongs2 = playlistSongsRepository.save(playlistSongs2);

        //when
        List<PlaylistSongs> playlistSongsList = playlistSongsRepository.findAll();

        //then
        assertThat(playlistSongsList).hasSize(2);
    }

    @Test
    public void findAllMPlaylistlistSongsByMonthlyPlaylistTest(){
        //given
        Playlist playlist = createPlaylist("techno playlist");
        playlist = playlistRepository.save(playlist);

        Song song1 = createSong("Weak","Weak","001","image01.png","image01.png");
        song1 = songRepository.save(song1);

        Song song2 = createSong("Cloud","Cloud","002","image02.png","image02.png");
        song2 = songRepository.save(song2);

        PlaylistSongs playlistSongs1 = createPlaylistSongs(playlist,song1);
        PlaylistSongs playlistSongs2 = createPlaylistSongs(playlist,song2);

        playlistSongs1 = playlistSongsRepository.save(playlistSongs1);
        playlistSongs2 = playlistSongsRepository.save(playlistSongs2);

        //when
        List<PlaylistSongs> playlistSongsList = playlistSongsRepository.findByPlaylistAndStatus(playlist,PlaylistSongsDTO.ENABLE);

        //then
        assertThat(playlistSongsList).hasSize(2);
    }

    @Test
    public void findAllPlaylistlistSongsBySongAndPlaylistTest(){
        //given
        Playlist playlist = createPlaylist("techno playlist");
        playlist = playlistRepository.save(playlist);

        Song song1 = createSong("Weak","Weak","001","image01.png","image01.png");
        song1 = songRepository.save(song1);

        Song song2 = createSong("Cloud","Cloud","002","image02.png","image02.png");
        song2 = songRepository.save(song2);

        PlaylistSongs playlistSongs1 = createPlaylistSongs(playlist,song1);
        PlaylistSongs playlistSongs2 = createPlaylistSongs(playlist,song2);

        playlistSongs1 = playlistSongsRepository.save(playlistSongs1);
        playlistSongs2 = playlistSongsRepository.save(playlistSongs2);

        //when
        playlistSongs1 = playlistSongsRepository.findBySongAndPlaylist(song1,playlist);
        playlistSongs2 = playlistSongsRepository.findBySongAndPlaylist(song2,playlist);

        //then
        assertThat(playlistSongs1.getSong().getBeatportName()).isEqualTo("Weak");
        assertThat(playlistSongs2.getSong().getBeatportName()).isEqualTo("Cloud");
    }


    @Test
    public void disableAllSongsTest(){
        //given
        Playlist playlist = createPlaylist("techno playlist");
        playlist = playlistRepository.save(playlist);

        Song song1 = createSong("Weak","Weak","001","image01.png","image01.png");
        song1 = songRepository.save(song1);

        Song song2 = createSong("Cloud","Cloud","002","image02.png","image02.png");
        song2 = songRepository.save(song2);

        PlaylistSongs playlistSongs1 = createPlaylistSongs(playlist,song1);
        PlaylistSongs playlistSongs2 = createPlaylistSongs(playlist,song2);

        playlistSongs1 = playlistSongsRepository.save(playlistSongs1);
        playlistSongs2 = playlistSongsRepository.save(playlistSongs2);

        //when
        playlistSongsRepository.disableAllSongs(playlistSongs1.getPlaylist().getId());
        List<PlaylistSongs> playlistSongsList = playlistSongsRepository.findAll();

        //then
        assertThat(playlistSongsList.get(0).getStatus()).isEqualTo(PlaylistSongsDTO.DISABLE);
        assertThat(playlistSongsList.get(1).getStatus()).isEqualTo(PlaylistSongsDTO.DISABLE);
    }
}
