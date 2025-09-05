package com.example.akshay.service;

import com.example.akshay.dto.AccountDto;
import com.example.akshay.dto.BalanceResponse;
import com.example.akshay.entity.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountDto createAccount(Account account);

    AccountDto getAccountByNumber(Integer accountNumber);

    AccountDto deposit(Integer accountNumber, BigDecimal amount);

    AccountDto withdraw(Integer accountNumber, BigDecimal amount);

    BalanceResponse getBalance(Integer accountNumber);

    List<AccountDto> getAccountsByPanCardNumber(String panCardNumber);

    void deleteAccountById(Integer accountNumber);

     List<AccountDto> getAllAccounts();
}