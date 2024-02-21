package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Artist;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ArtistRepositoryTest {
    @Autowired
    private ArtistRepository artistRepository;

    @Test
    public void saveArtistTest(){
        //given
        Artist artist = new Artist();
        artist.setBeatportName("Meduza");
        artist.setSpotifyName("Meduza");

        //when
        Artist savedArtist = artistRepository.save(artist);

        //then
        assertThat(savedArtist).isNotNull();
        assertThat(savedArtist.getId()).isGreaterThan(0);
    }

    @Test
    public void updateArtistTest(){
        //given
        Artist savedArtist = new Artist();
        savedArtist.setBeatportName("FISHER (OZ)");
        savedArtist.setSpotifyName("FISHER");
        Artist artist = artistRepository.save(savedArtist);
        //when
        artist.setSpotifyName("Fisher");
        artist = artistRepository.save(artist);

        //then
        assertThat(artist.getSpotifyName()).isNotEqualTo("FISHER");
        assertThat(artist.getSpotifyName()).isEqualTo("Fisher");
    }

    @Test
    public void findAllArtistsTest(){
        //given
        Artist savedArtist1 = new Artist();
        savedArtist1.setBeatportName("FISHER (OZ)");
        savedArtist1.setSpotifyName("FISHER");
        Artist artist1 = artistRepository.save(savedArtist1);

        Artist savedArtist2 = new Artist();
        savedArtist2.setBeatportName("Meduza");
        savedArtist2.setSpotifyName("Meduza");
        Artist artist2 = artistRepository.save(savedArtist2);
        //when
        List<Artist> artistList = artistRepository.findAll();

        //then
        assertThat(artistList).hasSize(2);
    }
    @Test
    public void findArtistsByIdTest(){
        //given
        Artist savedArtist1 = new Artist();
        savedArtist1.setBeatportName("FISHER (OZ)");
        savedArtist1.setSpotifyName("FISHER");
        Artist artist1 = artistRepository.save(savedArtist1);

        Artist savedArtist2 = new Artist();
        savedArtist2.setBeatportName("Meduza");
        savedArtist2.setSpotifyName("Meduza");
        Artist artist2 = artistRepository.save(savedArtist2);
        //when
        Optional<Artist> optional1 = artistRepository.findById(artist1.getId());
        Optional<Artist> optional2 = artistRepository.findById(artist2.getId());
        Optional<Artist> optional3 = artistRepository.findById(9999999L);

        //then
        assertThat(optional1).isPresent();
        assertThat(optional2).isPresent();
        assertThat(optional3).isPresent();
        assertThat(optional1.get().getId()).isEqualTo(artist1.getId());
        assertThat(optional2.get().getId()).isEqualTo(artist2.getId());

    }

    @Test
    public void findArtistsByNameTest(){
        //given
        Artist savedArtist1 = new Artist();
        savedArtist1.setBeatportName("FISHER (OZ)");
        savedArtist1.setSpotifyName("FISHER");
        Artist artist1 = artistRepository.save(savedArtist1);

        Artist savedArtist2 = new Artist();
        savedArtist2.setBeatportName("Meduza");
        savedArtist2.setSpotifyName("Meduza");
        Artist artist2 = artistRepository.save(savedArtist2);
        //when
        List<Artist> list1 = artistRepository.findByBeatportName(artist1.getBeatportName());
        List<Artist> list2 = artistRepository.findByBeatportName(artist2.getBeatportName());
        List<Artist> list3 = artistRepository.findByBeatportName("Diplo");

        //then
        assertThat(list1).isNotEmpty();
        assertThat(list2).isNotEmpty();
        assertThat(list3).isEmpty();
        assertThat(list1.get(0).getBeatportName()).isEqualTo("FISHER (OZ)");
        assertThat(list2.get(0).getBeatportName()).isEqualTo("Meduza");

    }
}
