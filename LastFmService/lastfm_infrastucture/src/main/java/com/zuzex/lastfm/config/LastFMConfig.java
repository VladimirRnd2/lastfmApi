package com.zuzex.lastfm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.lastfm.consumer.impl.KafkaConsumerImpl;
import com.zuzex.lastfm.usecase.album.AlbumService;
import com.zuzex.lastfm.usecase.album.AlbumServiceImpl;
import com.zuzex.lastfm.usecase.artist.ArtistService;
import com.zuzex.lastfm.usecase.artist.ArtistServiceImpl;
import com.zuzex.lastfm.usecase.genre.GenreService;
import com.zuzex.lastfm.usecase.genre.GenreServiceImpl;
import com.zuzex.lastfm.usecase.track.TrackService;
import com.zuzex.lastfm.usecase.track.TrackServiceImpl;
import com.zuzex.lastfm.usecase.track.port.KafkaConsumer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Configuration
@EnableKafka
public class LastFMConfig {

    @Bean
    public KafkaConsumer kafkaConsumer() {
        return new KafkaConsumerImpl(objectMapper(),trackService());
    }

    @Bean
    public TrackService trackService() {
        return new TrackServiceImpl(albumService(),artistService(),genreService());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ArtistService artistService() {
        return new ArtistServiceImpl();
    }

    @Bean
    public AlbumService albumService() {
        return new AlbumServiceImpl();
    }

    @Bean
    public GenreService genreService() {
        return new GenreServiceImpl();
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> replyContainer(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

        ConcurrentMessageListenerContainer<String, String> container = containerFactory.createContainer("bookRq");
        container.getContainerProperties().setGroupId(UUID.randomUUID().toString()); // unique
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // so the new group doesn't get old replies
        container.getContainerProperties().setKafkaConsumerProperties(props);
        container.getContainerProperties().setMessageListener(new MessageListener<String,String>() {
            @Override
            public void onMessage(ConsumerRecord<String, String> stringStringConsumerRecord) {

            }
        });
        return container;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public Map<String,String> topicConfig() {
        Map<String,String> map = new HashMap<>();
        map.put(TopicConfig.RETENTION_MS_CONFIG,"60000");
        return map;
    }

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("bookRq")
                .partitions(2)
                .replicas(2)
                .configs(topicConfig())
                .build();
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:29092");
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory < String, String > factory =
                new ConcurrentKafkaListenerContainerFactory < > ();
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkaTemplate());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:29092");
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "open");
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props,new StringDeserializer(),new StringDeserializer());
    }
}
