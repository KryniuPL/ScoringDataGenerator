package com.scoring.application.generator;

import com.scoring.application.producer.ClientSummaryProducer;
import com.scoring.application.repository.AccountsRepository;
import com.scoring.application.repository.ClientsRepository;
import com.scoring.application.repository.PaymentsRepository;
import com.scoring.domain.Account;
import com.scoring.domain.AccountStatus;
import com.scoring.domain.ClientSummary;
import com.scoring.domain.PaymentHistory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Singleton
public class ClientSummaryGenerator {

    @Inject
    ClientSummaryProducer clientSummaryProducer;

    @Inject
    ClientsRepository clientsRepository;

    @Inject
    AccountsRepository accountsRepository;

    @Inject
    PaymentsRepository paymentsRepository;

    @Inject
    private Clock clock;

    public void generateClientSummaries() {
        clientsRepository.getAllClientsUUIDS()
                .forEach(clientId -> {
                    List<Account> clientAccounts = accountsRepository.getAllByClientId(clientId);
                    List<PaymentHistory> paymentsHistory = clientAccounts.stream()
                            .flatMap(account -> paymentsRepository.getAllByAccountId(account.accountId()).stream())
                            .toList();

                    BigDecimal sumOfBalances = paymentsHistory
                            .stream()
                            .map(PaymentHistory::balance)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal maxOverdueAmount =  paymentsHistory
                            .stream()
                            .map(PaymentHistory::overdueAmount)
                            .max(Comparator.naturalOrder())
                            .orElseThrow();

                    Long maxDelayedDays = paymentsHistory
                            .stream()
                            .map(PaymentHistory::daysOfDelays)
                            .max(Comparator.naturalOrder())
                            .orElseThrow();

                    List<AccountStatus> lastStatuses = paymentsHistory
                            .stream()
                            .map(PaymentHistory::accountStatus)
                            .limit(6)
                            .toList();

                    AccountStatus worstStatus = paymentsHistory
                            .stream()
                            .map(PaymentHistory::accountStatus)
                            .max(Comparator.comparingInt(AccountStatus::getStatusScore))
                            .orElseThrow();

                    ClientSummary clientSummary = ClientSummary.builder()
                            .summaryId(UUID.randomUUID())
                            .clientId(clientId)
                            .creationDate(LocalDateTime.now(clock))
                            .sumOfBalances(sumOfBalances)
                            .lastStatuses(lastStatuses)
                            .maxOverdueAmount(maxOverdueAmount)
                            .maxDelayedDays(maxDelayedDays)
                            .worstStatus(worstStatus)
                            .accountTypes(clientAccounts.stream().map(Account::accountType).toList())
                            .build();

                    clientSummaryProducer.sendClientSummary(clientSummary.summaryId(), clientSummary);
                });
    }
}
