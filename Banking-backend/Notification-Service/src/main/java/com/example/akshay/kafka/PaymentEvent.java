package com.example.akshay.kafka;

import com.example.akshay.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {
    private String userEmail;
    private double amount;
    private PaymentStatus status;
    private String message;
    private Integer paymentId;
}
