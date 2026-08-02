package com.personalproject.demo.ingestion.service;

import java.util.UUID;

public interface PaymentProducer {
    public void sendBatchNotification(UUID batchId);
}
