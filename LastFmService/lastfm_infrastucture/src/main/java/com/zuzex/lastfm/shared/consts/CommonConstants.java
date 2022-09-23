package com.zuzex.lastfm.shared.consts;


import com.fasterxml.jackson.core.type.TypeReference;
import com.zuzex.lastfm.model.TrackResponse;

import java.util.List;

public class CommonConstants {

    public static final String KAFKA_BOOTSTRAP_SERVER_VALUE = "localhost:29092";
    public static final String KAFKA_GROUP_ID_VALUE = "foo";
    public static final String KAFKA_RESPONSE_TOPIC_VALUE = "bookRs";
    public static final String KAFKA_REQUEST_TOPIC_VALUE = "bookRq";
    public static final String KAFKA_LISTENER_CONTAINER_FACTORY = "kafkaListenerContainerFactory";

    public static final TypeReference<List<TrackResponse>> typeReference = new TypeReference<List<TrackResponse>>() {};
}
