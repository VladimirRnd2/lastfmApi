package com.zuzex.lastfm.consumer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.lastfm.consumer.dto.CreateTrackDto;
import com.zuzex.lastfm.model.TrackRs;
import com.zuzex.lastfm.usercase.track.CreateTrack;
import com.zuzex.lastfm.usercase.track.UpdateTrack;
import com.zuzex.lastfm.usercase.track.port.KafkaConsumer;
import de.umass.lastfm.Album;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.List;

@AllArgsConstructor
public class KafkaConsumerImpl implements KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final CreateTrack createTrack;
    private final UpdateTrack updateTrack;

    @Override
    @KafkaListener(topics = "bookRq", groupId = "foo", containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public String listen(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        String key = consumerRecord.key();
        switch (key) {
            case "track" -> {
                CreateTrackDto dtoTrack = objectMapper.readValue(consumerRecord.value(), CreateTrackDto.class);
                return objectMapper.writeValueAsString(List.of(createTrack.createNewTrack(dtoTrack.getSongName(), dtoTrack.getArtistName())));
            }
            case "album" -> {
                CreateTrackDto dtoAlbum = objectMapper.readValue(consumerRecord.value(), CreateTrackDto.class);
                return objectMapper.writeValueAsString(createTrack.createAlbumTracks(dtoAlbum.getSongName(), dtoAlbum.getArtistName()));
            }
            case "update" -> {
                List<TrackRs> trackRs = objectMapper.readValue(consumerRecord.value(), new TypeReference<List<TrackRs>>() {
                });
                return objectMapper.writeValueAsString(updateTrack.updateTracks(trackRs));
            }
        }
        return "Wrong method!!!";
    }
}
