package com.example.akshay.controller;

import com.example.akshay.dto.PaymentDto;
import com.example.akshay.dto.TransferRequest;
import com.example.akshay.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/transfer")
    public ResponseEntity<PaymentDto> processTransfer(@Valid @RequestBody TransferRequest transferRequest) {
        log.info("Received transfer request from {} to {} amount: {}",
                transferRequest.getSenderAccountNumber(),
                transferRequest.getReceiverAccountNumber(),
                transferRequest.getAmount());

        PaymentDto payment = paymentService.processTransfer(
                transferRequest.getSenderAccountNumber(),
                transferRequest.getReceiverAccountNumber(),
                transferRequest.getAmount());
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<PaymentDto>> getPaymentHistory(@PathVariable Integer accountNumber) {
        log.info("Received request for payment history of account: {}", accountNumber);
        List<PaymentDto> payments = paymentService.getPaymentHistory(accountNumber);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Integer paymentId) {
        log.info("Received request for payment ID: {}", paymentId);
        PaymentDto payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }
}