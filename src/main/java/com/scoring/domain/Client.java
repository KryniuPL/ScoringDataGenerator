package com.scoring.domain;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record Client(
        @KafkaKey UUID clientId,
        ClientJob clientJob,
        ClientMartialStatus clientMartialStatus,
        String firstName,
        String lastName,
        BigDecimal income,
        BigDecimal spending,
        Integer numberOfChildren,
        Integer age,
        String pesel,
        ClientType clientType
) {
}
