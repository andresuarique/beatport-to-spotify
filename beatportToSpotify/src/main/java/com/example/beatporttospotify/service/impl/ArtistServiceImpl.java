package com.example.beatporttospotify.service.impl;

import com.example.beatporttospotify.domain.Artist;
import com.example.beatporttospotify.dto.ArtistDTO;
import com.example.beatporttospotify.mapper.ArtistMapper;
import com.example.beatporttospotify.repository.ArtistRepository;
import com.example.beatporttospotify.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private ArtistMapper artistMapper;
    private static final Logger logger = LoggerFactory.getLogger(ArtistServiceImpl.class);
    @Override
    public List<ArtistDTO> getArtists() {
        return artistMapper.listArtistToListArtistDTO(artistRepository.findAll());
    }

    @Override
    public List<ArtistDTO> getArtistByNames(String name) {
        return artistMapper.listArtistToListArtistDTO(artistRepository.findByBeatportName(name));
    }

    @Override
    public ArtistDTO getArtistsById(Long id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        return optionalArtist.map(artist -> artistMapper.artistToArtistDTO(artist)).orElse(null);
    }

    @Override
    public ArtistDTO save(ArtistDTO artistDTO) {
        try{
            List<ArtistDTO> artistDTOS = getArtistByNames(artistDTO.getBeatportName());
            if(!artistDTOS.isEmpty())
                return artistDTOS.get(0);
            Artist  artist= artistMapper.artistDTOToArtist(artistDTO);
            artist = artistRepository.save(artist);
            return artistMapper.artistToArtistDTO(artist);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ArtistDTO update(ArtistDTO artistDTO) {
        try{
            Optional<Artist> optionalArtist = artistRepository.findById(artistDTO.getId());
            if(!optionalArtist.isPresent())
                return null;
            Artist artist = artistMapper.artistDTOToArtist(artistDTO);
            return artistMapper.artistToArtistDTO(artistRepository.save(artist));
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }
}
