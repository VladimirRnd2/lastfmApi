package com.zuzex.music.shared.constants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zuzex.music.model.Track;

import java.util.List;

public class CommonConstants {

    public static final String KAFKA_BOOTSTRAP_SERVER_VALUE = "localhost:29092";
    public static final String KAFKA_GROUP_ID_VALUE = "foo";
    public static final String KAFKA_RESPONSE_TOPIC_VALUE = "bookRs";
    public static final String KAFKA_REQUEST_TOPIC_VALUE = "bookRq";

    public static final TypeReference<List<Track>> typeReference = new TypeReference<List<Track>>() {};

    // вынести в отдельный утилитный класс констант
    public static final String CREATE_NEW_TRACK = "mono/track/create";
    public static final String GET_TRACK_BY_ID = "mono/track/id";
    public static final String GET_TRACK_BY_NAME_KEY = "mono/track/name";
    public static final String GET_ALL_TRACKS = "flux/track/all";
    public static final String GET_TRACKS_BY_GENRE = "flux/track/genre";
    public static final String GET_TRACKS_BY_ARTIST_NAME = "flux/track/artist";

}
