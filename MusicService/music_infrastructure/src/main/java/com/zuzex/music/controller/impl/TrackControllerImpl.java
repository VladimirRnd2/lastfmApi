package com.zuzex.music.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.music.controller.TrackController;
import com.zuzex.music.controller.requests.TrackCreateRequest;
import com.zuzex.music.model.Track;
import com.zuzex.music.usecase.track.CreateTrack;
import com.zuzex.music.usecase.track.FindTrack;
import com.zuzex.music.usecase.track.UpdateTrack;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/track")
@AllArgsConstructor
@Log
public class TrackControllerImpl implements TrackController {

    private final CreateTrack createTrack;
    private final FindTrack findTrack;
    private final UpdateTrack updateTrack;
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;


    @Override
    @PostMapping("/create")
    public Mono<Track> createTrack(@RequestBody TrackCreateRequest request) throws JsonProcessingException, ExecutionException, InterruptedException {
        String key = "mono/track/create";
        String hashKey = objectMapper.writeValueAsString(request);
        ReactiveHashOperations<String, String, String> opsForHash = reactiveStringRedisTemplate.opsForHash();
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("bookRq", "track", objectMapper.writeValueAsString(request));
        return opsForHash.get(key, hashKey).map(s -> {
            try {
                return objectMapper.readValue(s, Track.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).switchIfEmpty(createTrack
                .createTrack(objectMapper
                        .readValue(replyingKafkaTemplate
                                .sendAndReceive(producerRecord).get().value(), new TypeReference<List<Track>>() {
                        }).get(0))
                .doOnNext(track -> {
                    try {
                        reactiveStringRedisTemplate.scan(ScanOptions.scanOptions().match("flux/*").build()).flatMap(reactiveStringRedisTemplate::delete).subscribe();
                        opsForHash.put(key, hashKey, objectMapper.writeValueAsString(track)).subscribe();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                })
        );
    }

    // логика кэша - сначала проверяем кэш, есть ли у нас объект по ключу request.
    // Если есть - сразу возвращаем Flux<Track>, если нет - создаем объект и сохраняем его в кэш и очищаем методы получения списков Track
    @Override
    @PostMapping("/createByAlbum")
    public Flux<Track> createTracksByAlbum(@RequestBody TrackCreateRequest request) throws JsonProcessingException, ExecutionException, InterruptedException {
        // здесь необходимо добавить логику интеграции с кафкой, т.е. ReplyingKafkaTemplate.sendAndReceive()
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("bookRq", "album", objectMapper.writeValueAsString(request));
        producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "bookRs".getBytes(StandardCharsets.UTF_8)));
        RequestReplyFuture<String, String, String> future = replyingKafkaTemplate.sendAndReceive(producerRecord);
        ConsumerRecord<String, String> record = future.get();
        List<Track> trackList = objectMapper.readValue(record.value(), new TypeReference<List<Track>>() {
        });
        return createTrack.createTracksByAlbum(trackList);
    }

    @Override
    @GetMapping("/{id}")
    public Mono<Track> getTrackById(@PathVariable(name = "id") Long id) {
        String key = "mono/track/" + id;
        ReactiveHashOperations<String, String, String> opsForHash = reactiveStringRedisTemplate.opsForHash();
        return opsForHash.get(key, id.toString()).map(track -> {
            try {
                return objectMapper.readValue(track, Track.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).switchIfEmpty(findTrack.findById(id).doOnNext(track -> {
            try {
                opsForHash.put(key, track.getId().toString(), objectMapper.writeValueAsString(track)).subscribe();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    @GetMapping("/name")
    public Mono<Track> getTrackByName(@RequestParam(name = "track.name") String name) {
        String key = "mono/track/name" + name;
        ReactiveHashOperations<String, String, String> opsForHash = reactiveStringRedisTemplate.opsForHash();
        return opsForHash.get(key, name).map(track -> {
            try {
                return objectMapper.readValue(track, Track.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).switchIfEmpty(findTrack.findByName(name).doOnNext(track -> {
            try {
                opsForHash.put(key, name, objectMapper.writeValueAsString(track)).subscribe();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    @GetMapping("/")
    public Flux<Track> getAllTracks() {
        String key = "flux/track/all";
        ReactiveHashOperations<String, String, String> opsForHash = reactiveStringRedisTemplate.opsForHash();
        return opsForHash.get(key, key).map(str -> {
            try {
                return objectMapper.readValue(str, new TypeReference<List<Track>>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).flatMapMany(Flux::fromIterable).switchIfEmpty(findTrack
                .findAll()
                .collectList()
                .doOnNext(tracks -> {
                    try {
                        opsForHash.put(key, key, objectMapper.writeValueAsString(tracks)).subscribe();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }).flatMapMany(Flux::fromIterable));
    }

    @Override
    @GetMapping("/genre")
    public Flux<Track> getTracksByGenre(@RequestParam(name = "track.genre") String genre) {
        String key = "flux/track/genre";
        String hashKey = "flux/track/genre" + genre;
        ReactiveHashOperations<String, String, String> opsForHash = reactiveStringRedisTemplate.opsForHash();
        return opsForHash.get(key, genre).map(s -> {
                    try {
                        return objectMapper.readValue(s, new TypeReference<List<Track>>() {
                        });
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).flatMapMany(Flux::fromIterable)
                .switchIfEmpty(findTrack.findByGenre(genre).collectList().doOnNext(tracks -> {
                    try {
                        opsForHash.put(key, genre, objectMapper.writeValueAsString(tracks)).subscribe();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }).flatMapMany(Flux::fromIterable));
    }

    @Override
    @GetMapping("/artist")
    public Flux<Track> getTracksByArtist(@RequestParam(name = "artist.name") String artistName) {
        String key = "flux/track/artist";
        ReactiveHashOperations<String, String, String> opsForHash = reactiveStringRedisTemplate.opsForHash();
        return opsForHash.get(key, artistName).map(s -> {
                    try {
                        return objectMapper.readValue(s, new TypeReference<List<Track>>() {
                        });
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).flatMapMany(Flux::fromIterable)
                .switchIfEmpty(findTrack.findByArtistName(artistName).collectList().doOnNext(tracks -> {
                    try {
                        opsForHash.put(key, artistName, objectMapper.writeValueAsString(tracks)).subscribe();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }).flatMapMany(Flux::fromIterable));
    }

    @Async
    @Scheduled(fixedRate = 3600 * 1000)
    public void updateTracks() {
        reactiveStringRedisTemplate.scan(ScanOptions.scanOptions().match("*/track/*").build()).flatMap(reactiveStringRedisTemplate::delete).subscribe();
        ReactiveHashOperations<String, String, String> hashOperations = reactiveStringRedisTemplate.opsForHash();
        findTrack.findAll().collectList().map(tracks -> {
            try {
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>("bookRq", "update", objectMapper.writeValueAsString(tracks));
                producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "bookRs".getBytes(StandardCharsets.UTF_8)));
                RequestReplyFuture<String, String, String> future = replyingKafkaTemplate.sendAndReceive(producerRecord);
                future.addCallback(new ListenableFutureCallback<ConsumerRecord<String, String>>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        ex.printStackTrace();
                    }

                    @SneakyThrows
                    @Override
                    public void onSuccess(ConsumerRecord<String, String> result) {
                        List<Track> trackList = objectMapper.readValue(result.value(), new TypeReference<List<Track>>() {
                        });
                        System.out.println(trackList);
                        updateTrack.updateTracks(trackList);
                    }
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return tracks;
        }).subscribe();
    }
}
