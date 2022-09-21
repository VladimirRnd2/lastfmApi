package com.zuzex.music.config;

import com.zuzex.music.persistence.impl.AlbumStorageServiceImpl;
import com.zuzex.music.persistence.impl.ArtistStorageServiceImpl;
import com.zuzex.music.persistence.impl.GenreStorageServiceImpl;
import com.zuzex.music.persistence.impl.TrackStorageServiceImpl;
import com.zuzex.music.persistence.mapper.AlbumRepositoryMapper;
import com.zuzex.music.persistence.mapper.ArtistRepositoryMapper;
import com.zuzex.music.persistence.mapper.GenreRepositoryMapper;
import com.zuzex.music.persistence.mapper.TrackRepositoryMapper;
import com.zuzex.music.persistence.repository.*;
import com.zuzex.music.usecase.album.AlbumService;
import com.zuzex.music.usecase.album.AlbumServiceImpl;
import com.zuzex.music.usecase.album.port.AlbumStorageService;
import com.zuzex.music.usecase.artist.ArtistService;
import com.zuzex.music.usecase.artist.ArtistServiceImpl;
import com.zuzex.music.usecase.artist.port.ArtistStorageService;
import com.zuzex.music.usecase.chart.CreateChart;
import com.zuzex.music.usecase.chart.CreateChartImpl;
import com.zuzex.music.usecase.genre.*;
import com.zuzex.music.usecase.genre.port.GenreStorageService;
import com.zuzex.music.usecase.track.TrackService;
import com.zuzex.music.usecase.track.TrackServiceImpl;
import com.zuzex.music.usecase.track.port.TrackStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

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
    public CreateChart createChart() {
        return new CreateChartImpl(trackStorageService());
    }

    @Bean
    public TrackService trackService() {
        return new TrackServiceImpl(trackStorageService());
    }

    @Bean
    public AlbumService albumService() {
        return new AlbumServiceImpl(albumStorageService());
    }

    @Bean
    public ArtistService artistService() {
        return new ArtistServiceImpl(artistStorageService());
    }

    @Bean
    public GenreService genreService() {
        return new GenreServiceImpl(genreStorageService());
    }

    @Bean
    public TrackStorageService trackStorageService() {
        return new TrackStorageServiceImpl(
                trackReactiveRepository,
                trackGenreReactiveRepository,
                albumService(),
                artistService(),
                genreService(),
                trackRepositoryMapper());
    }

    @Bean
    public ArtistStorageService artistStorageService() {
        return new ArtistStorageServiceImpl(artistReactiveRepository, artistRepositoryMapper());
    }

    @Bean
    public AlbumStorageService albumStorageService() {
        return new AlbumStorageServiceImpl(albumReactiveRepository, albumRepositoryMapper());
    }

    @Bean
    public GenreStorageService genreStorageService() {
        return new GenreStorageServiceImpl(genreReactiveRepository, genreRepositoryMapper());
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

    @Bean
    public ReactiveHashOperations<String, String, String> opsForHash(ReactiveStringRedisTemplate template) {
        return template.opsForHash();
    }
}
