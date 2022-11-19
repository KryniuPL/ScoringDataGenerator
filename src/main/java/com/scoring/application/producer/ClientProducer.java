package com.scoring.application.producer;

import com.scoring.domain.Client;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

import java.util.UUID;

@KafkaClient
public interface ClientProducer {

    @Topic("clients")
    void sendClient(@KafkaKey UUID clientId, Client client);
}
