package com.example.akshay.service;

import com.example.akshay.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    PaymentDto processTransfer(Integer senderAccountNumber, Integer receiverAccountNumber, BigDecimal amount);

    List<PaymentDto> getPaymentHistory(Integer accountNumber);

    PaymentDto getPaymentById(Integer paymentId);
}