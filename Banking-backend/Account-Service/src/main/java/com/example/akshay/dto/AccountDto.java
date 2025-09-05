package com.example.akshay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Integer accountNumber;
    private String panCardNumber;
    private String username;
    private String accountType;
    private BigDecimal balance;
}