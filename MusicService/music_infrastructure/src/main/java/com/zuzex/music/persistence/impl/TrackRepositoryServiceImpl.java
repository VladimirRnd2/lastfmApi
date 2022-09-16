package com.zuzex.music.persistence.impl;

import com.zuzex.music.model.Album;
import com.zuzex.music.model.Artist;
import com.zuzex.music.model.Genre;
import com.zuzex.music.model.Track;
import com.zuzex.music.persistence.entity.*;
import com.zuzex.music.persistence.mapper.AlbumRepositoryMapper;
import com.zuzex.music.persistence.mapper.ArtistRepositoryMapper;
import com.zuzex.music.persistence.mapper.GenreRepositoryMapper;
import com.zuzex.music.persistence.mapper.TrackRepositoryMapper;
import com.zuzex.music.persistence.repository.*;
import com.zuzex.music.usecase.album.CreateAlbum;
import com.zuzex.music.usecase.album.FindAlbum;
import com.zuzex.music.usecase.artist.CreateArtist;
import com.zuzex.music.usecase.artist.FindArtist;
import com.zuzex.music.usecase.genre.CreateGenre;
import com.zuzex.music.usecase.genre.FindGenre;
import com.zuzex.music.usecase.track.port.TrackRepositoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class TrackRepositoryServiceImpl implements TrackRepositoryService {

    private final TrackReactiveRepository trackReactiveRepository;
//    private final ArtistReactiveRepository artistReactiveRepository;
//    private final AlbumReactiveRepository albumReactiveRepository;
//    private final GenreReactiveRepository genreReactiveRepository;
    private final TrackGenreReactiveRepository trackGenreReactiveRepository;
    private final TrackRepositoryMapper trackMapper;
    private final ArtistRepositoryMapper artistMapper;
    private final AlbumRepositoryMapper albumMapper;
    private final GenreRepositoryMapper genreMapper;
    private final CreateArtist createArtist;
    private final FindArtist findArtist;
    private final CreateAlbum createAlbum;
    private final FindAlbum findAlbum;
    private final CreateGenre createGenre;
    private final FindGenre findGenre;


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
                .switchIfEmpty(findArtist
                        .getArtistByName(track.getArtist().getName()).map(artistMapper::mapToEntity)
                        .switchIfEmpty(createArtist
                                .createArtist(track.getArtist()).map(artistMapper::mapToEntity))
                        .flatMap(
                                artistEntity -> createOrGetAlbumEntity(track,artistEntity))
                        .flatMap(album -> trackReactiveRepository
                                .save(mapToTrackEntityWithSave(track,album)))
                        .flatMap(trackEntity -> mapMonoTrackToTrack(Mono.just(trackEntity)))
                        .doOnNext(resultTrack -> createGenresByTrack(resultTrack.getGenres(), resultTrack.getId())));
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
    public void updateTracks(List<Track> trackFlux) {
        trackReactiveRepository.saveAll(trackFlux.stream().map(trackMapper::mapToEntity).collect(Collectors.toList())).subscribe();
    }

    private Mono<Track> mapMonoTrackToTrack(Mono<TrackEntity> trackEntityMono) {
        Mono<ArtistEntity> artistEntityMono = trackEntityMono.flatMap(trackEntity -> findArtist.getById(trackEntity.getArtistId())).map(artistMapper::mapToEntity);
        Mono<AlbumEntity> albumEntityMono = trackEntityMono.flatMap(trackEntity -> findAlbum.getById(trackEntity.getAlbumId())).map(albumMapper::mapToEntity);
        Mono<List<GenreEntity>> listMonoGenre = trackEntityMono.flatMap(trackEntity -> findGenre.findAllByTrackId(trackEntity.getId()).map(genreMapper::mapToEntity).collectList());
        return Mono.zip(trackEntityMono, artistEntityMono, albumEntityMono, listMonoGenre).map(tuple4 -> Track.builder()
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
            Mono<ArtistEntity> artistEntityMono = findArtist.getById(trackEntity.getArtistId()).map(artistMapper::mapToEntity);
            Mono<AlbumEntity> albumEntityMono = findAlbum.getById(trackEntity.getAlbumId()).map(albumMapper::mapToEntity);
            Mono<List<GenreEntity>> listMono = findGenre.findAllByTrackId(trackEntity.getId()).map(genreMapper::mapToEntity).collectList();
            Mono<Track> map = Mono.zip(artistEntityMono, albumEntityMono, listMono).map(tuple -> Track.builder()
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
            findGenre.getByName(genre.getName())
                    .switchIfEmpty(createGenre.createGenre(genre))
                    .map(genreEntity -> trackGenreReactiveRepository
                            .save(TrackGenreEntity.builder().trackId(trackId).genreId(genreEntity.getId()).build()).subscribe()).subscribe();
        });
    }

    private TrackEntity mapToTrackEntityWithSave(Track track, AlbumEntity albumEntity) {
        return TrackEntity.builder()
                .name(track.getName())
                .mbid(track.getMbid())
                .listeners(track.getListeners())
                .playcount(track.getPlaycount())
                .albumId(albumEntity.getId())
                .artistId(albumEntity.getArtistId())
                .build();
    }
    private Mono<AlbumEntity> createOrGetAlbumEntity(Track track, ArtistEntity artistEntity) {
        if (track.getAlbum().getName() != null) {
            return findAlbum
                    .getByName(track.getAlbum().getName())
                    .map(albumMapper::mapToEntity)
                    .switchIfEmpty(
                            createAlbum
                                    .createAlbum(track.getAlbum(),artistEntity.getId())
                                    .map(album -> {
                                        AlbumEntity albumEntity = albumMapper.mapToEntity(album);
                                        albumEntity.setArtistId(artistEntity.getId());
                                        return albumEntity;
                                    }));
        } else {
            return findAlbum
                    .getByName(track.getAlbum().getName())
                    .map(albumMapper::mapToEntity)
                    .switchIfEmpty(
                            createAlbum
                                    .createAlbum(track.getAlbum(),artistEntity.getId())
                                    .map(album -> {
                                        AlbumEntity albumEntity = albumMapper.mapToEntity(album);
                                        albumEntity.setArtistId(artistEntity.getId());
                                        return albumEntity;
                                    } ));
        }
    }
}

//        return getByName(track.getName())
//                .switchIfEmpty(artistReactiveRepository
//                        .findByName(track.getArtist().getName())
//                        .switchIfEmpty(artistReactiveRepository
//                                .save(ArtistEntity.builder()
//                                        .name(track.getArtist().getName())
//                                        .mbid(track.getArtist().getMbid())
//                                        .build()))
//                        .flatMap(
//                                artistEntity -> {
//                                    if (track.getAlbum().getName() != null) {
//                                        return albumReactiveRepository
//                                                .findByName(track.getAlbum().getName())
//                                                .switchIfEmpty(
//                                                        albumReactiveRepository
//                                                                .save(
//                                                                        AlbumEntity.builder()
//                                                                                .name(track.getAlbum().getName())
//                                                                                .mbid(track.getAlbum().getMbid())
//                                                                                .artistId(artistEntity.getId())
//                                                                                .build()));
//                                    } else {
//                                        return albumReactiveRepository
//                                                .findByName(track.getAlbum().getName())
//                                                .switchIfEmpty(
//                                                        albumReactiveRepository
//                                                                .save(
//                                                                        AlbumEntity.builder()
//                                                                                .name("No Album")
//                                                                                .mbid("No Album")
//                                                                                .artistId(artistEntity.getId())
//                                                                                .build()));
//                                    }
//                                })
//                        .flatMap(album -> trackReactiveRepository
//                                .save(TrackEntity.builder()
//                                        .name(track.getName())
//                                        .mbid(track.getMbid())
//                                        .listeners(track.getListeners())
//                                        .playcount(track.getPlaycount())
//                                        .albumId(album.getId())
//                                        .artistId(album.getArtistId())
//                                        .build()))
//                        .map(trackEntity -> Track.builder()
//                                .id(trackEntity.getId())
//                                .name(trackEntity.getName())
//                                .mbid(trackEntity.getMbid())
//                                .listeners(trackEntity.getListeners())
//                                .playcount(trackEntity.getPlaycount())
//                                .artist(Artist.builder().name(track.getArtist().getName()).mbid(track.getArtist().getMbid()).id(trackEntity.getArtistId()).build())
//                                .album(Album.builder().id(trackEntity.getAlbumId()).name(track.getAlbum().getName()).mbid(track.getAlbum().getMbid()).build())
//                                .genres(track.getGenres())
//                                .build())
//                        .doOnNext(resultTrack -> resultTrack.getGenres()
//                                .forEach(genre -> {
//                                    genreReactiveRepository.findByName(genre.getName())
//                                            .switchIfEmpty(genreReactiveRepository.save(GenreEntity.builder().name(genre.getName()).build()))
//                                            .map(genreEntity -> trackGenreReactiveRepository
//                                                    .save(TrackGenreEntity.builder().trackId(resultTrack.getId()).genreId(genreEntity.getId()).build()).subscribe()).subscribe();
//                                })));
