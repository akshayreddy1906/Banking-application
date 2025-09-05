package com.example.akshay.kafka;

import com.example.akshay.entity.Payment.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEvent {
    private String userEmail;
    private double amount;
    private PaymentStatus status; 
    private String message;
    private Integer paymentId;
}
