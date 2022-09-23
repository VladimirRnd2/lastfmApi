package com.zuzex.lastfm.usecase.track.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface KafkaConsumer {

    String listen(ConsumerRecord<String,String> consumerRecord) throws JsonProcessingException;
}
