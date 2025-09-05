package com.example.akshay.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void sendPaymentEvent(PaymentEvent paymentEvent) {
        try {
            kafkaTemplate.send("payment-topic", paymentEvent);
            log.info("Payment event sent to Kafka: {}", paymentEvent.getPaymentId());
        } catch (Exception e) {
            log.error("Failed to send payment event to Kafka: {}", e.getMessage());
        }
    }
}
