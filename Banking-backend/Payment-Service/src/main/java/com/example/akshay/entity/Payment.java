package com.example.akshay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @NotBlank(message = "Sender email is required")
    @Email(message = "Valid sender email is required")
    private String senderEmail; // Changed from senderName

    @NotNull(message = "Sender account number is required")
    private Integer senderAccountNumber;

    @NotBlank(message = "Receiver email is required")
    @Email(message = "Valid receiver email is required")
    private String receiverEmail; // Changed from receiverName

    @NotNull(message = "Receiver account number is required")
    private Integer receiverAccountNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public enum PaymentStatus {
        SUCCESS, FAILURE
    }
}
