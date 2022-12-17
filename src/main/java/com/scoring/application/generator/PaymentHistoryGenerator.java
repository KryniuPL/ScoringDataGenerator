package com.scoring.application.generator;

import com.scoring.application.producer.PaymentHistoryProducer;
import com.scoring.application.supplier.PaymentHistorySupplier;
import com.scoring.application.utils.RequestHolder;
import com.scoring.domain.Account;
import com.scoring.domain.PaymentHistory;
import jakarta.inject.Inject;

public class PaymentHistoryGenerator {

    @Inject
    private PaymentHistoryProducer paymentHistoryProducer;

    @Inject
    private PaymentHistorySupplier paymentHistorySupplier;

    public void generatePaymentsHistory(Account account) {
        Long numberOfPayments = RequestHolder.getDataGenerationRequest().numberOfPaymentsPerAccount();
        for (int i = 0; i < numberOfPayments; i++) {
            PaymentHistory paymentHistory = paymentHistorySupplier.get(account);
            paymentHistoryProducer.sendPaymentHistory(paymentHistory.paymentId(), paymentHistory);
        }
    }
}
