package com.zuzex.music.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.music.controller.TrackController;
import com.zuzex.music.controller.requests.TrackCreateRequest;
import com.zuzex.music.model.Track;
import com.zuzex.music.usecase.track.TrackService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/track")
@RequiredArgsConstructor
public class TrackControllerImpl implements TrackController {

    private final TrackService trackService;
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;
    private final ReactiveHashOperations<String, String, String> opsForHash;

    private static final TypeReference<List<Track>> typeReference = new TypeReference<List<Track>>() {};

    // вынести в отдельный утилитный класс констант
    private static final String CREATE_NEW_TRACK = "mono/track/create";
    private static final String GET_TRACK_BY_ID = "mono/track/id";
    private static final String GET_TRACK_BY_NAME_KEY = "mono/track/name";
    private static final String GET_ALL_TRACKS = "flux/track/all";
    private static final String GET_TRACKS_BY_GENRE = "flux/track/genre";
    private static final String GET_TRACKS_BY_ARTIST_NAME = "flux/track/artist";
    private static final String BOOK_REQUEST_TOPIC_NAME = "bookRq";


    @Override
    @PostMapping("/create")
    public Mono<Track> createTrack(@RequestBody TrackCreateRequest request) throws JsonProcessingException {
        String hashKey = objectMapper.writeValueAsString(request);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(BOOK_REQUEST_TOPIC_NAME, "track", objectMapper.writeValueAsString(request));
        return opsForHash.get(CREATE_NEW_TRACK, hashKey)
                .flatMap(this::readMonoTrackFromJson)
                .onErrorMap(exception -> new RuntimeException(exception.getMessage()))
                .switchIfEmpty(Mono.defer(() ->
                        readMonoTrackListFromJsonCreate(producerRecord)
                                .flatMap(trackList -> trackService.createTrack(trackList.get(0)))
                                .flatMap(track -> writeMonoTrackListToJsonAndPutToCacheCreate(track, CREATE_NEW_TRACK, hashKey))
                ))
                .onErrorMap(exception -> new RuntimeException(exception.getMessage()));
    }

    @Override
    @PostMapping("/createByAlbum")
    public Flux<Track> createTracksByAlbum(@RequestBody TrackCreateRequest request) throws JsonProcessingException, ExecutionException, InterruptedException {

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(BOOK_REQUEST_TOPIC_NAME, "album", objectMapper.writeValueAsString(request));
        RequestReplyFuture<String, String, String> future = replyingKafkaTemplate.sendAndReceive(producerRecord);
        ConsumerRecord<String, String> record = future.get();
        List<Track> trackList = objectMapper.readValue(record.value(), typeReference);
        return trackService.createTracksByAlbum(trackList);
    }

    @Override
    @GetMapping("/{id}")
    public Mono<Track> getTrackById(@PathVariable(name = "id") Long id) {
        return opsForHash.get(GET_TRACK_BY_ID, id.toString())
                .flatMap(this::readMonoTrackFromJson)
                .onErrorMap(exception -> new RuntimeException(exception.getMessage()))
                .switchIfEmpty(Mono.defer(() ->
                        trackService.findById(id)
                                .flatMap(track -> writeMonoTrackToJsonAndPutToCacheById(track, GET_TRACK_BY_ID))))
                .onErrorMap(exception -> new RuntimeException(exception.getMessage()));
    }

    @Override
    @GetMapping("/name")
    public Mono<Track> getTrackByName(@RequestParam(name = "track.name") String name) { // некорректное название параметра track.name . Какое имя будет корректно?
        return opsForHash.get(GET_TRACK_BY_NAME_KEY, name)
                .flatMap(this::readMonoTrackFromJson)
                .onErrorMap(e -> new RuntimeException(e.getMessage()))
                .switchIfEmpty(Mono.defer(() ->
                        trackService.findByName(name)
                                .flatMap(track -> writeMonoTrackToJsonAndPutToCache(track, GET_TRACK_BY_NAME_KEY))))
                .onErrorMap(e -> new RuntimeException(e.getMessage()));
    }

    @Override
    @GetMapping("/")
    public Flux<Track> getAllTracks() {
        return opsForHash.get(GET_ALL_TRACKS, GET_ALL_TRACKS)
                .flatMap(this::readMonoTrackListFromJson)
                .flatMapMany(Flux::fromIterable)
                .onErrorMap(e -> new RuntimeException(e.getMessage()))
                .switchIfEmpty(Flux.defer(() -> trackService.findAll()
                        .collectList()
                        .flatMap(tracks -> writeMonoTrackListToJsonAndPutToCache(tracks, GET_ALL_TRACKS, GET_ALL_TRACKS))
                        .flatMapMany(Flux::fromIterable)))
                .onErrorMap(e -> new RuntimeException(e.getMessage()));
    }

    @Override
    @GetMapping("/genre")
    public Flux<Track> getTracksByGenre(@RequestParam(name = "track.genre") String genre) {
        return opsForHash.get(GET_TRACKS_BY_GENRE, genre)
                .flatMap(this::readMonoTrackListFromJson)
                .onErrorMap(ex -> new RuntimeException(ex.getMessage()))
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(Flux.defer(() -> trackService.findByGenre(genre)
                        .collectList()
                        .flatMap(tracks -> writeMonoTrackListToJsonAndPutToCache(tracks, GET_TRACKS_BY_GENRE, genre))
                        .flatMapMany(Flux::fromIterable)))
                .onErrorMap(e -> new RuntimeException(e.getMessage()));
    }

    @Override
    @GetMapping("/artist")
    public Flux<Track> getTracksByArtist(@RequestParam(name = "artist.name") String artistName) {
        return opsForHash.get(GET_TRACKS_BY_ARTIST_NAME, artistName)
                .flatMap(this::readMonoTrackListFromJson)
                .onErrorMap(exception -> new RuntimeException(exception.getMessage()))
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(Flux.defer(() -> trackService.findByArtistName(artistName)
                        .collectList()
                        .flatMap(trackList -> writeMonoTrackListToJsonAndPutToCache(trackList, GET_TRACKS_BY_ARTIST_NAME, artistName))
                        .flatMapMany(Flux::fromIterable)))
                .onErrorMap(exception -> new RuntimeException(exception.getMessage()));
    }

    @Async
    @Scheduled(fixedRate = 3600 * 1000)
    public void updateTracks() {
        reactiveStringRedisTemplate
                .scan(ScanOptions.scanOptions().match("*/track/*").build())
                .flatMap(reactiveStringRedisTemplate::delete)
                .then(trackService.findAll()
                        .collectList()
                        .flatMap(tracks -> Mono.fromCallable(() -> {
                            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(BOOK_REQUEST_TOPIC_NAME, "update", objectMapper.writeValueAsString(tracks));
                            ConsumerRecord<String, String> record = replyingKafkaTemplate.sendAndReceive(producerRecord).get();
                            List<Track> trackList = objectMapper.readValue(record.value(), typeReference);
                            System.out.println(trackList);
                            trackService.updateTracks(trackList);
                            return tracks;
                        })))
                .subscribe();
    }

    private Mono<Track> readMonoTrackFromJson(String jsonTrack) {
        return Mono.fromCallable(() -> objectMapper.readValue(jsonTrack, Track.class));
    }

    private Mono<Track> writeMonoTrackToJsonAndPutToCache(Track track, String key) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(track))
                .flatMap(string -> opsForHash.put(key, track.getName(), string))
                .thenReturn(track);
    }

    private Mono<Track> writeMonoTrackToJsonAndPutToCacheById(Track track, String key) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(track))
                .flatMap(string -> opsForHash.put(key, track.getId().toString(), string))
                .thenReturn(track);
    }

    private Mono<List<Track>> readMonoTrackListFromJson(String jsonTrackList) {
        return Mono.fromCallable(() -> objectMapper.readValue(jsonTrackList, typeReference));
    }

    private Mono<List<Track>> writeMonoTrackListToJsonAndPutToCache(List<Track> trackList, String key, String hKey) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(trackList))
                .flatMap(string -> opsForHash.put(key, hKey, string))
                .thenReturn(trackList);
    }

    private Mono<List<Track>> readMonoTrackListFromJsonCreate(ProducerRecord<String, String> producerRecord) {
        return Mono.fromCallable(() -> replyingKafkaTemplate.sendAndReceive(producerRecord).get().value())
                .flatMap(s -> Mono.fromCallable(() -> objectMapper.readValue(s, typeReference)));
    }

    private Mono<Track> writeMonoTrackListToJsonAndPutToCacheCreate(Track track, String key, String hKey) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(track))
                .flatMap(string -> reactiveStringRedisTemplate
                        .scan(ScanOptions.scanOptions().match("flux/*").build())
                        .flatMap(reactiveStringRedisTemplate::delete)
                        .flatMap(map -> opsForHash.put(key, hKey, string))
                        .collectList()
                        .thenReturn(track));
    }
}
