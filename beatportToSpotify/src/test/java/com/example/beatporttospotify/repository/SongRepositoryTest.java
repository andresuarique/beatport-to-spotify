package com.example.beatporttospotify.repository;
import com.example.beatporttospotify.domain.Song;
import com.example.beatporttospotify.dto.SongDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class SongRepositoryTest {

    @Autowired
    private SongRepository songRepository;

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

    @Test
    public void saveSongTest(){
        //given
        Song song = createSong("Weak","Weak",
                "0001","imageB.png","imageS.png");

        //when
        Song savedSong = songRepository.save(song);

        //then
        assertThat(savedSong).isNotNull();
        assertThat(savedSong.getId()).isGreaterThan(0);
    }

    @Test
    public void updateSongTest(){
        //given
        Song song = createSong("Weak","Weak",
                "0001","imageB.png","imageS.png");
        Song savedSong = songRepository.save(song);

        //when
        savedSong.setSpotifyName("Weak ft. Vintage Culture");
        savedSong = songRepository.save(song);

        //then
        assertThat(savedSong.getSpotifyName()).isNotEqualTo("Weak");
        assertThat(savedSong.getSpotifyName()).isEqualTo("Weak ft. Vintage Culture");
    }

    @Test
    public void findAllSongsTest(){
        //given
        Song song1 = createSong("Weak","Weak",
                "0001","imageB.png","imageS.png");
        Song savedSong1 = songRepository.save(song1);

        Song song2 = createSong("Miles","Miles",
                "0002","imageB001.png","imageS001.png");
        Song savedSong2 = songRepository.save(song2);

        //when
        List<Song> songList = songRepository.findAll();

        //then
        assertThat(songList).hasSize(2);
    }

    @Test
    public void findSongByBeatportNameTest(){
        //given
        Song song1 = createSong("Weak","Weak",
                "0001","imageB.png","imageS.png");
        Song savedSong1 = songRepository.save(song1);

        Song song2 = createSong("Miles","Miles",
                "0002","imageB001.png","imageS001.png");
        Song savedSong2 = songRepository.save(song2);

        //when
        List<Song> list1 = songRepository.findByBeatportName("Weak");
        List<Song> list2 = songRepository.findByBeatportName("Miles");
        List<Song> list3 = songRepository.findByBeatportName("Cloud");

        //then
        assertThat(list1).isNotEmpty();
        assertThat(list2).isNotEmpty();
        assertThat(list3).isEmpty();
        assertThat(list1.get(0).getBeatportName()).isEqualTo(savedSong1.getBeatportName());
        assertThat(list2.get(0).getBeatportName()).isEqualTo(savedSong2.getBeatportName());
    }
}
