package com.example.akshay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.akshay.enums.TransactionType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    private Long auditId;
    private Integer paymentId;
    private Integer senderAccount;
    private Integer receiverAccount;
    private BigDecimal amount;
    private String status;
    private TransactionType transactionType;
    private LocalDateTime timestamp;
}
