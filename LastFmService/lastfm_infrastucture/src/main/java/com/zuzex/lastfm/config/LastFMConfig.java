package com.zuzex.lastfm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.lastfm.consumer.impl.KafkaConsumerImpl;
import com.zuzex.lastfm.usecase.album.AlbumService;
import com.zuzex.lastfm.usecase.album.AlbumServiceImpl;
import com.zuzex.lastfm.usecase.artist.ArtistService;
import com.zuzex.lastfm.usecase.artist.ArtistServiceImpl;
import com.zuzex.lastfm.usecase.genre.GenreService;
import com.zuzex.lastfm.usecase.genre.GenreServiceImpl;
import com.zuzex.lastfm.usecase.track.TrackService;
import com.zuzex.lastfm.usecase.track.TrackServiceImpl;
import com.zuzex.lastfm.usecase.track.port.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LastFMConfig {

    @Bean
    public KafkaConsumer kafkaConsumer() {
        return new KafkaConsumerImpl(objectMapper(), trackService());
    }

    @Bean
    public TrackService trackService() {
        return new TrackServiceImpl(albumService(), artistService(), genreService());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ArtistService artistService() {
        return new ArtistServiceImpl();
    }

    @Bean
    public AlbumService albumService() {
        return new AlbumServiceImpl();
    }

    @Bean
    public GenreService genreService() {
        return new GenreServiceImpl();
    }

}
