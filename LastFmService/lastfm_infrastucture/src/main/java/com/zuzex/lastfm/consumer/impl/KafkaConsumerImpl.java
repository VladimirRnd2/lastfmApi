package com.zuzex.lastfm.consumer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.lastfm.consumer.dto.CreateTrackDto;
import com.zuzex.lastfm.model.TrackResponse;
import com.zuzex.lastfm.usecase.track.TrackService;
import com.zuzex.lastfm.usecase.track.port.KafkaConsumer;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.List;

import static com.zuzex.lastfm.shared.consts.CommonConstants.*;

@AllArgsConstructor
public class KafkaConsumerImpl implements KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final TrackService trackService;

    @Override
    @KafkaListener(topics = KAFKA_REQUEST_TOPIC_VALUE, groupId = KAFKA_GROUP_ID_VALUE, containerFactory = KAFKA_LISTENER_CONTAINER_FACTORY)
    @SendTo
    public String listen(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        String key = consumerRecord.key();
        switch (key) {
            case "track" -> {
                CreateTrackDto dtoTrack = objectMapper.readValue(consumerRecord.value(), CreateTrackDto.class);
                return objectMapper.writeValueAsString(List.of(trackService.createNewTrack(dtoTrack.getSongName(), dtoTrack.getArtistName())));
            }
            case "album" -> {
                CreateTrackDto dtoAlbum = objectMapper.readValue(consumerRecord.value(), CreateTrackDto.class);
                return objectMapper.writeValueAsString(trackService.createAlbumTracks(dtoAlbum.getSongName(), dtoAlbum.getArtistName()));
            }
            case "update" -> {
                List<TrackResponse> trackRS = objectMapper.readValue(consumerRecord.value(), typeReference);
                return objectMapper.writeValueAsString(trackService.updateTracks(trackRS));
            }
        }
        throw new RuntimeException("Wrong Method!");
    }
}
