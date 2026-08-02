package com.personalproject.demo.ingestion.service.serviceImpl;

import com.personalproject.demo.ingestion.config.RabbitMQConfig;
import com.personalproject.demo.ingestion.service.PaymentProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentProducerImpl implements PaymentProducer {

    private final RabbitTemplate rabbitTemplate;
    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;
    @Value("{app.rabbitmq.routing-key}")
    private String routingKey;

    @Override
    public void sendBatchNotification(UUID batchId) {
        log.info("Producing message for batch Id {}", batchId);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, batchId.toString());
    }
}
