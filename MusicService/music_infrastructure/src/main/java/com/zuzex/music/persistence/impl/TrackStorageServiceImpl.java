package com.zuzex.music.persistence.impl;

import com.zuzex.music.model.Album;
import com.zuzex.music.model.Artist;
import com.zuzex.music.model.Genre;
import com.zuzex.music.model.Track;
import com.zuzex.music.persistence.entity.TrackEntity;
import com.zuzex.music.persistence.entity.TrackGenreEntity;
import com.zuzex.music.persistence.mapper.TrackMapper;
import com.zuzex.music.persistence.repository.TrackGenreReactiveRepository;
import com.zuzex.music.persistence.repository.TrackReactiveRepository;
import com.zuzex.music.usecase.album.AlbumService;
import com.zuzex.music.usecase.artist.ArtistService;
import com.zuzex.music.usecase.genre.GenreService;
import com.zuzex.music.usecase.track.port.TrackStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class TrackStorageServiceImpl implements TrackStorageService {

    private final TrackReactiveRepository trackReactiveRepository;
    private final TrackGenreReactiveRepository trackGenreReactiveRepository;
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final GenreService genreService;
    private final TrackMapper trackMapper;


    @Override
    public Flux<Track> getAllTracks() {
        return mapFluxTrackEntityToFluxTrack(trackReactiveRepository.findAll());
    }

    @Override
    public Flux<Track> getByGenre(String genre) {
        return mapFluxTrackEntityToFluxTrack(trackReactiveRepository.findAllByGenresName(genre));
    }

    @Override
    public Flux<Track> getByArtistId(Long artistId) {
        return mapFluxTrackEntityToFluxTrack(trackReactiveRepository.findByArtistId(artistId));
    }

    @Override
    public Mono<Track> saveTrack(Track track) {
        return getByName(track.getName())
                .switchIfEmpty(artistService.getArtistByName(track.getArtist().getName())
                        .switchIfEmpty(artistService
                                .createArtist(track.getArtist()))
                        .flatMap(
                                artistEntity -> createOrGetAlbumEntity(track, artistEntity)
                                        .flatMap(album -> trackReactiveRepository
                                                .save(trackMapper.mapToTrackEntityWithSave(track, album, artistEntity.getId()))))
                        .flatMap(trackEntity -> createGenresByTrackAndReturnTrack(track.getGenres(), trackEntity.getId())));
    }

    @Override
    public Flux<Track> saveAll(List<Track> trackList) {
        return Flux.fromIterable(trackList).flatMap(this::saveTrack);
    }

    @Override
    public Mono<Track> getByName(String name) {
        return trackReactiveRepository.findByName(name).flatMap(this::mapMonoTrackToTrack);
    }

    @Override
    public Mono<Track> getById(Long id) {
        return trackReactiveRepository.findById(id).flatMap(this::mapMonoTrackToTrack);
    }

    @Override
    public Flux<Track> getByArtistName(String artistName) {
        return mapFluxTrackEntityToFluxTrack(trackReactiveRepository.findByArtistName(artistName));
    }

    @Override
    public Flux<Track> createChartByListeners() {
        return mapFluxTrackEntityToFluxTrack(trackReactiveRepository.findTrackByListeners());
    }

    @Override
    public Flux<Track> createChartByPlayCounts() {
        return mapFluxTrackEntityToFluxTrack(trackReactiveRepository.findTrackByPlayCounts());
    }

    @Override
    public void updateTracks(List<Track> trackList) {
        trackReactiveRepository.saveAll(trackList.stream().map(trackMapper::mapToEntity).collect(Collectors.toList())).subscribe();
    }

    private Mono<Track> mapMonoTrackToTrack(TrackEntity trackEntity) {
        Mono<Artist> artistMono = artistService.getById(trackEntity.getArtistId());
        Mono<Album> albumMono = albumService.getById(trackEntity.getAlbumId());
        Mono<List<Genre>> listGenreMono = genreService.findAllByTrackId(trackEntity.getId()).collectList();
        return trackMapper.mapToTrackFromTrackEntity(trackEntity, artistMono, albumMono, listGenreMono);
    }

    private Flux<Track> mapFluxTrackEntityToFluxTrack(Flux<TrackEntity> trackEntityFlux) {
        return trackEntityFlux.flatMap(trackEntity -> {
            Mono<Artist> artistMono = artistService.getById(trackEntity.getArtistId());
            Mono<Album> albumMono = albumService.getById(trackEntity.getAlbumId());
            Mono<List<Genre>> listGenreMono = genreService.findAllByTrackId(trackEntity.getId()).collectList();
            return trackMapper.mapToTrackFromTrackEntity(trackEntity, artistMono, albumMono, listGenreMono);
        });
    }

    private Mono<Track> createGenresByTrackAndReturnTrack(List<Genre> genres, Long trackId) {
        return Flux.fromIterable(genres)
                .flatMap(genre ->
                        genreService.getByName(genre.getName())
                                .switchIfEmpty(Mono.defer(() -> genreService.createGenre(genre)))
                                .flatMap(genreEntity -> trackGenreReactiveRepository
                                        .save(TrackGenreEntity.builder().trackId(trackId).genreId(genreEntity.getId()).build())))
                .flatMap(trackGenreEntity -> trackReactiveRepository.findById(trackGenreEntity.getTrackId()))
                .collectList()
                .map(trackEntities -> trackEntities.get(0))
                .flatMap(this::mapMonoTrackToTrack);
    }

    private Mono<Album> createOrGetAlbumEntity(Track track, Artist artist) {
        if (track.getAlbum().getName() != null) {
            return albumService
                    .getByName(track.getAlbum().getName())
                    .switchIfEmpty(Mono.defer(() -> albumService
                            .createAlbum(track.getAlbum(), artist.getId())));
        } else {
            return albumService
                    .createAlbum(Album.builder()
                                    .name("No Album")
                                    .build(),
                            artist.getId());
        }
    }
}
