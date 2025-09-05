package com.example.akshay.service;

import com.example.akshay.dto.AccountDto;
import com.example.akshay.dto.PaymentDto;
import com.example.akshay.dto.TransactionRequest;
import com.example.akshay.entity.Payment;
import com.example.akshay.entity.Payment.PaymentStatus;
import com.example.akshay.exception.AccountNotFoundException;
import com.example.akshay.exception.InsufficientBalanceException;
import com.example.akshay.exception.PaymentNotFoundException;
import com.example.akshay.exception.PaymentProcessingException;
import com.example.akshay.feign.AccountServiceClient;
import com.example.akshay.feign.AuditServiceClient;
import com.example.akshay.kafka.PaymentEventProducer;
import com.example.akshay.kafka.PaymentEvent;
import com.example.akshay.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.akshay.feign.CustomerServClient;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountServiceClient accountServiceClient;
    private final AuditServiceClient auditServiceClient;
    private final PaymentEventProducer paymentEventProducer;

    private final CustomerServClient customerServClient;
    
    @Override
    @Transactional
    public PaymentDto processTransfer(Integer senderAccountNumber, Integer receiverAccountNumber, BigDecimal amount) {
        log.info("Processing transfer from {} to {} amount: {}", 
                senderAccountNumber, receiverAccountNumber, amount);
        
        Payment payment = new Payment();
        payment.setSenderAccountNumber(senderAccountNumber);
        payment.setReceiverAccountNumber(receiverAccountNumber);
        payment.setAmount(amount);
        payment.setTimestamp(LocalDateTime.now());
        
        try {
            // Step 1: Validate sender account and get sender email
            AccountDto senderAccount = accountServiceClient.getAccountByNumber(senderAccountNumber).getBody();
            if (senderAccount == null) {
                throw new AccountNotFoundException("Sender account not found: " + senderAccountNumber);
            }
            
            // Get sender email from Customer Service
            String senderEmail = customerServClient.getEmailByPan(senderAccount.getPanCardNumber()).getBody();
            if (senderEmail == null) {
                throw new AccountNotFoundException("Sender email not found for PAN: " + senderAccount.getPanCardNumber());
            }
            payment.setSenderEmail(senderEmail); // Store email of sender
            
            // Step 2: Validate receiver account and get receiver email
            AccountDto receiverAccount = accountServiceClient.getAccountByNumber(receiverAccountNumber).getBody();
            if (receiverAccount == null) {
                throw new AccountNotFoundException("Receiver account not found: " + receiverAccountNumber);
            }
            
            // Get receiver email from Customer Service
            String receiverEmail = customerServClient.getEmailByPan(receiverAccount.getPanCardNumber()).getBody();
            if (receiverEmail == null) {
                throw new AccountNotFoundException("Receiver email not found for PAN: " + receiverAccount.getPanCardNumber());
            }
            payment.setReceiverEmail(receiverEmail); // Store email of receiver
            
            // Step 3: Check sender balance
            if (senderAccount.getBalance().compareTo(amount) < 0) {
                payment.setStatus(Payment.PaymentStatus.FAILURE);
                Payment savedFailedPayment = paymentRepository.save(payment);
                
                // Publish failed payment event to Kafka with email
                publishPaymentEvent(savedFailedPayment, senderEmail);
                
                throw new InsufficientBalanceException("Insufficient balance in sender account");
            }
            
            // Step 4: Debit sender
            accountServiceClient.withdraw(senderAccountNumber, new TransactionRequest(amount));
            
            // Step 5: Credit receiver
            accountServiceClient.deposit(receiverAccountNumber, new TransactionRequest(amount));
            
            // Step 6: Update payment status
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            Payment savedPayment = paymentRepository.save(payment);
            
            // Step 7: Log to Audit Service
            logToAuditService(savedPayment);
            
            // Step 8: Publish successful payment event to Kafka with email
            publishPaymentEvent(savedPayment, senderEmail);
            
            log.info("Transfer completed successfully. Payment ID: {}", savedPayment.getPaymentId());
            return convertToDto(savedPayment);
            
        } catch (AccountNotFoundException | InsufficientBalanceException e) {
            payment.setStatus(Payment.PaymentStatus.FAILURE);
            paymentRepository.save(payment);
            throw e;
        } catch (Exception e) {
            payment.setStatus(Payment.PaymentStatus.FAILURE);
            Payment savedFailedPayment = paymentRepository.save(payment);
            
            // Publish failed payment event to Kafka
            try {
                AccountDto senderAccount = accountServiceClient.getAccountByNumber(senderAccountNumber).getBody();
                String senderEmail = customerServClient.getEmailByPan(senderAccount.getPanCardNumber()).getBody();
                publishPaymentEvent(savedFailedPayment, senderEmail);
            } catch (Exception kafkaException) {
                log.warn("Failed to publish payment event to Kafka: {}", kafkaException.getMessage());
            }
            
            log.error("Error processing transfer: {}", e.getMessage(), e);
            throw new PaymentProcessingException("Failed to process transfer", e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentHistory(Integer accountNumber) {
        log.info("Fetching payment history for account: {}", accountNumber);
        List<Payment> payments = paymentRepository
                .findBySenderAccountNumberOrReceiverAccountNumberOrderByTimestampDesc(
                        accountNumber, accountNumber);
        return payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(Integer paymentId) {
        log.info("Fetching payment with ID: {}", paymentId);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));
        return convertToDto(payment);
    }

    private void publishPaymentEvent(Payment payment, String senderEmail) {
        try {
            PaymentEvent paymentEvent = PaymentEvent.builder()
                    .userEmail(senderEmail)
                    .amount(payment.getAmount().doubleValue())
                    .status(payment.getStatus() == Payment.PaymentStatus.SUCCESS ? 
                           PaymentStatus.SUCCESS : PaymentStatus.FAILURE)
                    .message(createPaymentMessage(payment))
                    .paymentId(payment.getPaymentId())
                    .build();

            paymentEventProducer.sendPaymentEvent(paymentEvent);
            log.info("Payment event published to Kafka for payment ID: {}", payment.getPaymentId());
        } catch (Exception e) {
            log.warn("Failed to publish payment event to Kafka: {}", e.getMessage());
        }
    }


    private String createPaymentMessage(Payment payment) {
        if (payment.getStatus() == Payment.PaymentStatus.SUCCESS) {
            return String.format("Your payment of %s from account %d to account %d is successful!", 
                    payment.getAmount(), payment.getSenderAccountNumber(), payment.getReceiverAccountNumber());
        } else {
            return String.format("Payment of %s from account %d to account %d failed!", 
                    payment.getAmount(), payment.getSenderAccountNumber(), payment.getReceiverAccountNumber());
        }
    }

    private void logToAuditService(Payment payment) {
        try {
            Map<String, Object> auditData = new HashMap<>();
            auditData.put("paymentId", payment.getPaymentId());
            auditData.put("senderAccount", payment.getSenderAccountNumber());
            auditData.put("receiverAccount", payment.getReceiverAccountNumber());
            auditData.put("amount", payment.getAmount());
            auditData.put("timestamp", payment.getTimestamp());
            auditData.put("status", payment.getStatus());
            auditData.put("transactionType", "TRANSFER");

            auditServiceClient.logTransaction(auditData);
            log.info("Transaction logged to audit service for payment: {}", payment.getPaymentId());
        } catch (Exception e) {
            log.warn("Failed to log to audit service: {}", e.getMessage());
        }
    }

    private PaymentDto convertToDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(payment.getPaymentId());
        dto.setSenderEmail(payment.getSenderEmail());
        dto.setSenderAccountNumber(payment.getSenderAccountNumber());
        dto.setReceiverEmail(payment.getReceiverEmail());
        dto.setReceiverAccountNumber(payment.getReceiverAccountNumber());
        dto.setAmount(payment.getAmount());
        dto.setTimestamp(payment.getTimestamp());
        dto.setStatus(payment.getStatus());
        return dto;
    }
}
