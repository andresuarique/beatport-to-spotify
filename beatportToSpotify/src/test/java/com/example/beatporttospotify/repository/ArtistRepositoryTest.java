package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Artist;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
