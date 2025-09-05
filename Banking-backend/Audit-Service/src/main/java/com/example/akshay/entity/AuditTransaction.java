package com.example.akshay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import com.example.akshay.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_transactions")
@Immutable  
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long auditId;

    @Column( nullable = true,updatable = false)
    private Integer paymentId;

    @Column(updatable = false)
    private Integer senderAccount;

    @Column(updatable = false)
    private Integer receiverAccount;

    @Column(precision = 15, scale = 2, nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType transactionType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
