package com.example.akshay.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.akshay.kafka.PaymentEvent;
import com.example.akshay.service.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic", groupId = "notification-group")
    public void consumePaymentEvent(PaymentEvent paymentEvent) {
        log.info("Received payment event: {}", paymentEvent);
        emailService.sendPaymentEmail(paymentEvent);
    }
}
