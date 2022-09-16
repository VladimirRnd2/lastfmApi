package com.zuzex.music.config;

import com.zuzex.music.persistence.impl.AlbumRepositoryServiceImpl;
import com.zuzex.music.persistence.impl.ArtistRepositoryServiceImpl;
import com.zuzex.music.persistence.impl.GenreRepositoryServiceImpl;
import com.zuzex.music.persistence.impl.TrackRepositoryServiceImpl;
import com.zuzex.music.persistence.mapper.AlbumRepositoryMapper;
import com.zuzex.music.persistence.mapper.ArtistRepositoryMapper;
import com.zuzex.music.persistence.mapper.GenreRepositoryMapper;
import com.zuzex.music.persistence.mapper.TrackRepositoryMapper;
import com.zuzex.music.persistence.repository.*;
import com.zuzex.music.usecase.album.CreateAlbum;
import com.zuzex.music.usecase.album.CreateAlbumImpl;
import com.zuzex.music.usecase.album.FindAlbum;
import com.zuzex.music.usecase.album.FindAlbumImpl;
import com.zuzex.music.usecase.album.port.AlbumRepositoryService;
import com.zuzex.music.usecase.artist.CreateArtist;
import com.zuzex.music.usecase.artist.CreateArtistImpl;
import com.zuzex.music.usecase.artist.FindArtist;
import com.zuzex.music.usecase.artist.FindArtistImpl;
import com.zuzex.music.usecase.artist.port.ArtistRepositoryService;
import com.zuzex.music.usecase.chart.CreateChart;
import com.zuzex.music.usecase.chart.CreateChartImpl;
import com.zuzex.music.usecase.genre.CreateGenre;
import com.zuzex.music.usecase.genre.CreateGenreImpl;
import com.zuzex.music.usecase.genre.FindGenre;
import com.zuzex.music.usecase.genre.FindGenreImpl;
import com.zuzex.music.usecase.genre.port.GenreRepositoryService;
import com.zuzex.music.usecase.track.*;
import com.zuzex.music.usecase.track.port.TrackRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MusicServiceConfiguration {

    @Autowired
    public TrackReactiveRepository trackReactiveRepository;

    @Autowired
    public AlbumReactiveRepository albumReactiveRepository;

    @Autowired
    public ArtistReactiveRepository artistReactiveRepository;

    @Autowired
    public GenreReactiveRepository genreReactiveRepository;

    @Autowired
    public TrackGenreReactiveRepository trackGenreReactiveRepository;


    @Bean
    public CreateTrack createTrack() {
        return new CreateTrackImpl(trackRepositoryService());
    }

    @Bean
    public FindTrack findTrack() {
        return new FindTrackImpl(trackRepositoryService());
    }

    @Bean
    public UpdateTrack updateTrack() {
        return new UpdateTrackImpl(trackRepositoryService());
    }

    @Bean
    public FindArtist findArtist() {
        return new FindArtistImpl(artistRepositoryService());
    }

    @Bean
    public CreateArtist createArtist() {
        return new CreateArtistImpl(artistRepositoryService());
    }

    @Bean
    public CreateAlbum createAlbum() {
        return new CreateAlbumImpl(albumRepositoryService());
    }

    @Bean
    public FindAlbum findAlbum() {
        return new FindAlbumImpl(albumRepositoryService());
    }

    @Bean
    public CreateGenre createGenre() {
        return new CreateGenreImpl(genreRepositoryService());
    }

    @Bean
    public FindGenre findGenre() {
        return new FindGenreImpl(genreRepositoryService());
    }

    @Bean
    public CreateChart createChart() {
        return new CreateChartImpl(trackRepositoryService());
    }

    @Bean
    public TrackRepositoryService trackRepositoryService() {
        return new TrackRepositoryServiceImpl(
                trackReactiveRepository,
                trackGenreReactiveRepository,
                trackRepositoryMapper(),
                artistRepositoryMapper(),
                albumRepositoryMapper(),
                genreRepositoryMapper(),
                createArtist(),
                findArtist(),
                createAlbum(),
                findAlbum(),
                createGenre(),
                findGenre());
    }

    @Bean
    public ArtistRepositoryService artistRepositoryService() {
        return new ArtistRepositoryServiceImpl(artistReactiveRepository, artistRepositoryMapper());
    }

    @Bean
    public AlbumRepositoryService albumRepositoryService() {
        return new AlbumRepositoryServiceImpl(albumReactiveRepository, albumRepositoryMapper());
    }

    @Bean
    public GenreRepositoryService genreRepositoryService() {
        return new GenreRepositoryServiceImpl(genreReactiveRepository, genreRepositoryMapper());
    }

    @Bean
    public TrackRepositoryMapper trackRepositoryMapper() {
        return new TrackRepositoryMapper();
    }

    @Bean
    public ArtistRepositoryMapper artistRepositoryMapper() {
        return new ArtistRepositoryMapper();
    }

    @Bean
    public AlbumRepositoryMapper albumRepositoryMapper() {
        return new AlbumRepositoryMapper();
    }

    @Bean
    public GenreRepositoryMapper genreRepositoryMapper() {
        return new GenreRepositoryMapper();
    }
}
