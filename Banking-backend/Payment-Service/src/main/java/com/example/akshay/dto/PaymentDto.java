package com.example.akshay.dto;

import com.example.akshay.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Integer paymentId;
    private String senderEmail;
    private Integer senderAccountNumber;
    private String receiverEmail; 
    private Integer receiverAccountNumber;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private Payment.PaymentStatus status;
}
