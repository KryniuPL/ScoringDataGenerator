package com.scoring.domain;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PaymentHistory(
        UUID paymentId,
        Account account,
        Client client,
        AccountType accountType,
        LocalDateTime date,
        BigDecimal balance,
        Long daysOfDelays,
        BigDecimal overdueAmount,
        AccountStatus accountStatus
) {
}
