package com.zuzex.lastfm.usercase.track.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zuzex.lastfm.model.TrackRs;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface KafkaConsumer {

    String listen(ConsumerRecord<String,String> consumerRecord) throws JsonProcessingException;
}
