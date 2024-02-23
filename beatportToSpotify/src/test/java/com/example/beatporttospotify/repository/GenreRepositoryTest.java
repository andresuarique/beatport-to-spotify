package com.example.beatporttospotify.repository;

import com.example.beatporttospotify.domain.Genre;
import com.example.beatporttospotify.dto.GenreDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    private Genre createGenre(String code, String name){
        Genre genre = new Genre();
        genre.setCode(code);
        genre.setName(name);
        genre.setStatus(GenreDTO.ENABLE);
        genre.setUrl(name+"/"+code);
        return genre;
    }

    @Test
    public void saveGenreTest(){
        //given
        Genre genre = createGenre("1","techno");

        //when
        Genre saveGenre = genreRepository.save(genre);

        //then
        assertThat(saveGenre).isNotNull();
        assertThat(saveGenre.getId()).isGreaterThan(0);
    }

    @Test
    public void updateGenreTest(){
        //given
        Genre genre = createGenre("1","techno");
        Genre savedGenre = genreRepository.save(genre);

        //when
        savedGenre.setName("house");
        savedGenre = genreRepository.save(savedGenre);

        //then
        assertThat(savedGenre.getName()).isNotEqualTo("techno");
        assertThat(savedGenre.getName()).isEqualTo("house");
    }

    @Test
    public void findAllGenresTest(){
        //given
        Genre genre1 = createGenre("1","techno");
        Genre savedGenre1 = genreRepository.save(genre1);

        Genre genre2 = createGenre("2","house");
        Genre savedGenre2 = genreRepository.save(genre2);

        //when
        List<Genre> genreList = genreRepository.findAll();

        //then
        assertThat(genreList).hasSize(2);
    }

    @Test
    public void findByNameTest(){
        //given
        Genre genre1 = createGenre("1","techno");
        Genre savedGenre1 = genreRepository.save(genre1);

        Genre genre2 = createGenre("2","house");
        Genre savedGenre2 = genreRepository.save(genre2);

        //when
        List<Genre> list1 = genreRepository.findByName(savedGenre1.getName());
        List<Genre> list2 = genreRepository.findByName(savedGenre2.getName());
        List<Genre> list3 = genreRepository.findByName("trance");

        //then
        assertThat(list1).isNotEmpty();
        assertThat(list2).isNotEmpty();
        assertThat(list3).isEmpty();
        assertThat(list1.get(0).getName()).isEqualTo(genre1.getName());
        assertThat(list2.get(0).getName()).isEqualTo(genre2.getName());
    }

    @Test
    public void findByUrlTest(){
        //given
        Genre genre1 = createGenre("1","techno");
        Genre savedGenre1 = genreRepository.save(genre1);

        Genre genre2 = createGenre("2","house");
        Genre savedGenre2 = genreRepository.save(genre2);

        //when
        List<Genre> list1 = genreRepository.findByUrl("techno/1");
        List<Genre> list2 = genreRepository.findByUrl("house/2");
        List<Genre> list3 = genreRepository.findByUrl("trance/5");

        //then
        assertThat(list1).isNotEmpty();
        assertThat(list2).isNotEmpty();
        assertThat(list3).isEmpty();
        assertThat(list1.get(0).getUrl()).isEqualTo(genre1.getUrl());
        assertThat(list2.get(0).getUrl()).isEqualTo(genre2.getUrl());
    }

    @Test
    public void findByCodeTest(){
        //given
        Genre genre1 = createGenre("1","techno");
        Genre savedGenre1 = genreRepository.save(genre1);

        Genre genre2 = createGenre("2","house");
        Genre savedGenre2 = genreRepository.save(genre2);

        //when
        List<Genre> list1 = genreRepository.findByCode("1");
        List<Genre> list2 = genreRepository.findByCode("2");
        List<Genre> list3 = genreRepository.findByCode("5");

        //then
        assertThat(list1).isNotEmpty();
        assertThat(list2).isNotEmpty();
        assertThat(list3).isEmpty();
        assertThat(list1.get(0).getCode()).isEqualTo(genre1.getCode());
        assertThat(list2.get(0).getCode()).isEqualTo(genre2.getCode());
    }

    @Test
    public void findByNameAndCodeTest(){
        //given
        Genre genre1 = createGenre("1","techno");
        Genre savedGenre1 = genreRepository.save(genre1);

        Genre genre2 = createGenre("2","house");
        Genre savedGenre2 = genreRepository.save(genre2);

        //when
        List<Genre> list1 = genreRepository.findByNameAndCode("techno","1");
        List<Genre> list2 = genreRepository.findByNameAndCode("house","2");
        List<Genre> list3 = genreRepository.findByNameAndCode("trance","5");

        //then
        assertThat(list1).isNotEmpty();
        assertThat(list2).isNotEmpty();
        assertThat(list3).isEmpty();
        assertThat(list1.get(0).getName()).isEqualTo(genre1.getName());
        assertThat(list2.get(0).getName()).isEqualTo(genre2.getName());
        assertThat(list1.get(0).getCode()).isEqualTo(genre1.getCode());
        assertThat(list2.get(0).getCode()).isEqualTo(genre2.getCode());
    }

}
