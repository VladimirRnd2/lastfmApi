package com.zuzex.lastfm.config;

import com.zuzex.lastfm.shared.consts.CommonConstants;
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

import static com.zuzex.lastfm.shared.consts.CommonConstants.*;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KAFKA_BOOTSTRAP_SERVER_VALUE);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                KAFKA_GROUP_ID_VALUE);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props,new StringDeserializer(),new StringDeserializer());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory< String, String > factory =
                new ConcurrentKafkaListenerContainerFactory < > ();
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkaTemplate());
        return factory;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KAFKA_BOOTSTRAP_SERVER_VALUE);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> replyContainer(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

        ConcurrentMessageListenerContainer<String, String> container = containerFactory.createContainer(KAFKA_REQUEST_TOPIC_VALUE);
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
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVER_VALUE);
        return new KafkaAdmin(configs);
    }

    @Bean
    public Map<String,String> topicConfig() {
        Map<String,String> map = new HashMap<>();
        map.put(TopicConfig.RETENTION_MS_CONFIG,"60000");
        return map;
    }

    @Bean
    public NewTopic topicRequest() {
        return TopicBuilder.name(KAFKA_REQUEST_TOPIC_VALUE)
                .partitions(2)
                .replicas(2)
                .configs(topicConfig())
                .build();
    }
}
