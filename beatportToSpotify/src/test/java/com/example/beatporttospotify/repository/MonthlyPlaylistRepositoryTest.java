package com.example.beatporttospotify.repository;
import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.domain.MonthlyPlaylist;
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
public class MonthlyPlaylistRepositoryTest {

    @Autowired
    private MonthlyPlaylistRepository monthlyPlaylistRepository;

    @Autowired
    private GenreRepository genreRepository;

    private MonthlyPlaylist createMonthlyPlaylist(String playlistName,Genre genre){
        MonthlyPlaylist monthlyPlaylist = new MonthlyPlaylist();
        monthlyPlaylist.setName(playlistName);
        monthlyPlaylist.setCreationDate(new Date());
        monthlyPlaylist.setModificationDate(null);
        monthlyPlaylist.setGenre(genre);
        return monthlyPlaylist;
    }

    private Genre createGenre(String name, String code){
        Genre genre = new Genre();
        genre.setName(name);
        genre.setCode(code);
        genre.setStatus(GenreDTO.ENABLE);
        return genre;
    }

    @Test
    public void saveMonthlyPlaylistTest(){
        //given
        Genre genre = createGenre("techno","1");
        genre = genreRepository.save(genre);
        MonthlyPlaylist monthlyPlaylist = createMonthlyPlaylist("techno playlist",genre);

        //when
        MonthlyPlaylist savedMonthlyPlaylist = monthlyPlaylistRepository.save(monthlyPlaylist);

        //then
        assertThat(savedMonthlyPlaylist).isNotNull();
        assertThat(savedMonthlyPlaylist.getId()).isGreaterThan(0);
    }

    @Test
    public void updateMonthlyPlaylistTest(){
        //given
        Genre genre = createGenre("techno","1");
        genre = genreRepository.save(genre);
        MonthlyPlaylist monthlyPlaylist = createMonthlyPlaylist("techno playlist",genre);
        MonthlyPlaylist savedMonthlyPlaylist = monthlyPlaylistRepository.save(monthlyPlaylist);

        //when
        savedMonthlyPlaylist.setName("Techno Top 100");
        savedMonthlyPlaylist = monthlyPlaylistRepository.save(savedMonthlyPlaylist);

        //then
        assertThat(savedMonthlyPlaylist.getName()).isNotEqualTo("techno playlist");
        assertThat(savedMonthlyPlaylist.getName()).isEqualTo("Techno Top 100");
    }

    @Test
    public void findAllMonthlyPlaylistsTest(){
        //given
        Genre genre = createGenre("techno","1");
        genre = genreRepository.save(genre);
        MonthlyPlaylist monthlyPlaylist1 = createMonthlyPlaylist("techno playlist",genre);
        MonthlyPlaylist savedMonthlyPlaylist1 = monthlyPlaylistRepository.save(monthlyPlaylist1);
        MonthlyPlaylist monthlyPlaylist2 = createMonthlyPlaylist("techno top 100",genre);
        MonthlyPlaylist savedMonthlyPlaylist2 = monthlyPlaylistRepository.save(monthlyPlaylist2);

        //when
        List<MonthlyPlaylist> monthlyPlaylistList = monthlyPlaylistRepository.findAll();

        //then
        assertThat(monthlyPlaylistList).hasSize(2);
    }

    @Test
    public void findMonthlyPlaylistsByGenreAndCreationDateTest(){
        //given
        Genre genre = createGenre("techno","1");
        genre = genreRepository.save(genre);
        MonthlyPlaylist monthlyPlaylist1 = createMonthlyPlaylist("techno playlist",genre);
        MonthlyPlaylist savedMonthlyPlaylist1 = monthlyPlaylistRepository.save(monthlyPlaylist1);
        Date date1 = savedMonthlyPlaylist1.getCreationDate();

        //when
        MonthlyPlaylist monthlyPlaylistResult1 = monthlyPlaylistRepository.findByGenreAndCreationDate(genre,date1);
        MonthlyPlaylist monthlyPlaylistResult2 = monthlyPlaylistRepository.findByGenreAndCreationDate(genre,null);

        //then
        assertThat(monthlyPlaylistResult2).isNull();
        assertThat(monthlyPlaylistResult1).isNotNull();
        assertThat(monthlyPlaylistResult1.getName()).isEqualTo("techno playlist");
    }
}
