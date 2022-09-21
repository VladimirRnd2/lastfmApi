package com.zuzex.music.persistence.impl;

import com.zuzex.music.model.Album;
import com.zuzex.music.model.Artist;
import com.zuzex.music.model.Genre;
import com.zuzex.music.model.Track;
import com.zuzex.music.persistence.entity.TrackEntity;
import com.zuzex.music.persistence.entity.TrackGenreEntity;
import com.zuzex.music.persistence.mapper.TrackRepositoryMapper;
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
    private final TrackRepositoryMapper trackMapper;


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
                .switchIfEmpty(artistService
                        .getArtistByName(track.getArtist().getName())
                        .switchIfEmpty(artistService
                                .createArtist(track.getArtist()))
                        .flatMap(
                                artistEntity -> createOrGetAlbumEntity(track, artistEntity))
                        .flatMap(album -> trackReactiveRepository
                                .save(mapToTrackEntityWithSave(track, album)))
                        .flatMap(trackEntity -> mapMonoTrackToTrack(Mono.just(trackEntity)))
                        .doOnNext(resultTrack -> createGenresByTrack(track.getGenres(), resultTrack.getId())));
    }

    @Override
    public Flux<Track> saveAll(List<Track> trackList) {
        return Flux.fromIterable(trackList).flatMap(this::saveTrack);
    }

    @Override
    public Mono<Track> getByName(String name) {
        return mapMonoTrackToTrack(trackReactiveRepository.findByName(name));
    }

    @Override
    public Mono<Track> getById(Long id) {
        return mapMonoTrackToTrack(trackReactiveRepository.findById(id));
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

    private Mono<Track> mapMonoTrackToTrack(Mono<TrackEntity> trackEntityMono) {
        Mono<Artist> artistMono = trackEntityMono.flatMap(trackEntity -> artistService.getById(trackEntity.getArtistId()));
        Mono<Album> albumMono = trackEntityMono.flatMap(trackEntity -> albumService.getById(trackEntity.getAlbumId()));
        Mono<List<Genre>> listGenreMono = trackEntityMono.flatMap(trackEntity -> genreService.findAllByTrackId(trackEntity.getId()).collectList());
        return Mono.zip(trackEntityMono, artistMono, albumMono, listGenreMono).map(tuple4 -> Track.builder()
                .id(tuple4.getT1().getId())
                .name(tuple4.getT1().getName())
                .mbid(tuple4.getT1().getMbid())
                .playcount(tuple4.getT1().getPlaycount())
                .listeners(tuple4.getT1().getListeners())
                .artist(Artist.builder()
                        .id(tuple4.getT2().getId())
                        .name(tuple4.getT2().getName())
                        .mbid(tuple4.getT2().getMbid())
                        .build())
                .album(Album.builder()
                        .id(tuple4.getT3().getId())
                        .name(tuple4.getT3().getName())
                        .mbid(tuple4.getT3().getMbid())
                        .build())
                .genres(tuple4.getT4().stream().map(genreEntity -> Genre.builder()
                                .id(genreEntity.getId())
                                .name(genreEntity.getName())
                                .build())
                        .collect(Collectors.toList()))
                .build());
    }

    private Flux<Track> mapFluxTrackEntityToFluxTrack(Flux<TrackEntity> trackEntityFlux) {

        return trackEntityFlux.flatMap(trackEntity -> {
            Mono<Artist> artistMono = artistService.getById(trackEntity.getArtistId());
            Mono<Album> albumMono = albumService.getById(trackEntity.getAlbumId());
            Mono<List<Genre>> listGenreMono = genreService.findAllByTrackId(trackEntity.getId()).collectList();
            Mono<Track> map = Mono.zip(artistMono, albumMono, listGenreMono).map(tuple -> Track.builder()
                    .id(trackEntity.getId())
                    .name(trackEntity.getName())
                    .mbid(trackEntity.getMbid())
                    .playcount(trackEntity.getPlaycount())
                    .listeners(trackEntity.getListeners())
                    .artist(Artist.builder()
                            .id(trackEntity.getArtistId())
                            .name(tuple.getT1().getName())
                            .mbid(tuple.getT1().getMbid())
                            .build())
                    .album(Album.builder()
                            .id(trackEntity.getAlbumId())
                            .name(tuple.getT2().getName())
                            .mbid(tuple.getT2().getMbid())
                            .build())
                    .genres(tuple.getT3().stream().map(genreEntity -> Genre.builder().id(genreEntity.getId()).name(genreEntity.getName()).build()).collect(Collectors.toList()))
                    .build());
            return map;
        });
    }

    private void createGenresByTrack(List<Genre> genres, Long trackId) {
        genres.forEach(genre -> {
            genreService.getByName(genre.getName())
                    .switchIfEmpty(Mono.defer(() -> genreService.createGenre(genre)))
                    .flatMap(genreEntity -> trackGenreReactiveRepository
                            .save(TrackGenreEntity.builder().trackId(trackId).genreId(genreEntity.getId()).build()))
                    .subscribe();
        });
    }

    private TrackEntity mapToTrackEntityWithSave(Track track, Album albumEntity) {
        return TrackEntity.builder()
                .name(track.getName())
                .mbid(track.getMbid())
                .listeners(track.getListeners())
                .playcount(track.getPlaycount())
                .albumId(albumEntity.getId())
                .artistId(albumEntity.getArtist().getId())
                .build();
    }

    private Mono<Album> createOrGetAlbumEntity(Track track, Artist artist) {
        if (track.getAlbum().getName() != null) {
            return albumService
                    .createAlbum(track.getAlbum(), artist.getId());
        } else {
            return albumService
                    .getByName(track.getAlbum().getName())
                    .switchIfEmpty(
                            albumService
                                    .createAlbum(track.getAlbum(), artist.getId()));
        }
    }
}
