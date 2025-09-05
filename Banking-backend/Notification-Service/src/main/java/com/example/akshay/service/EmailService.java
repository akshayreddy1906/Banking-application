package com.example.akshay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.akshay.kafka.PaymentEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPaymentEmail(PaymentEvent paymentEvent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(paymentEvent.getUserEmail());
            message.setSubject("Payment Status - " + paymentEvent.getStatus().name());
            message.setText(paymentEvent.getMessage() + 
                           "\n\nAmount: ₹" + paymentEvent.getAmount() +
                           "\nPayment ID: " + paymentEvent.getPaymentId());

            mailSender.send(message);
            log.info("Email sent successfully to: {}", paymentEvent.getUserEmail());
            
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", paymentEvent.getUserEmail(), e.getMessage());
        }
    }
}
