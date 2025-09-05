package com.example.akshay.service.impl;

import com.example.akshay.dto.AccountDto;
import com.example.akshay.dto.BalanceResponse;
import com.example.akshay.entity.Account;
import com.example.akshay.exception.AccountAlreadyExistsException;
import com.example.akshay.exception.AccountNotFoundException;
import com.example.akshay.exception.InsufficientBalanceException;
import com.example.akshay.feign.CustomerServiceClient;
import com.example.akshay.feign.AuditServiceClient;
import com.example.akshay.repository.AccountRepository;
import com.example.akshay.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerServiceClient customerServiceClient;
    private final AuditServiceClient auditServiceClient;

    @Override
    public AccountDto createAccount(Account account) {
        log.info("Creating account for PAN: {}", account.getPanCardNumber());
        
        try {
            customerServiceClient.getCustomerByPanCardNumber(account.getPanCardNumber());
        } catch (Exception e) {
            throw new AccountNotFoundException("Customer not found with PAN: " + account.getPanCardNumber());
        }

        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new AccountAlreadyExistsException("Username already exists: " + account.getUsername());
        }

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with number: {}", savedAccount.getAccountNumber());
        return convertToDto(savedAccount);
    }

    @Override
    public AccountDto getAccountByNumber(Integer accountNumber) {
        log.info("Fetching account with number: {}", accountNumber);
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        return convertToDto(account);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        log.info("Fetching all accounts");
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public AccountDto deposit(Integer accountNumber, BigDecimal amount) {
        log.info("Depositing {} to account: {}", amount, accountNumber);
        
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        
        BigDecimal oldBalance = account.getBalance();
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Deposit successful. New balance: {}", newBalance);
        
        // Log to audit service
        logToAuditService(accountNumber, null, amount, "SUCCESS", "DEPOSIT");
        
        return convertToDto(updatedAccount);
    }

    @Override
    public AccountDto withdraw(Integer accountNumber, BigDecimal amount) {
        log.info("Withdrawing {} from account: {}", amount, accountNumber);
        
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        
        if (account.getBalance().compareTo(amount) < 0) {
            // Log failed withdrawal to audit
            logToAuditService(accountNumber, null, amount, "FAILURE", "WITHDRAWAL");
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: " + account.getBalance() + ", Requested: " + amount);
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Withdrawal successful. New balance: {}", newBalance);
        
        // Log successful withdrawal to audit
        logToAuditService(accountNumber, null, amount, "SUCCESS", "WITHDRAWAL");
        
        return convertToDto(updatedAccount);
    }

    @Override
    public BalanceResponse getBalance(Integer accountNumber) {
        log.info("Fetching balance for account: {}", accountNumber);
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        return new BalanceResponse(accountNumber, account.getBalance(), "Balance retrieved successfully");
    }

    @Override
    public List<AccountDto> getAccountsByPanCardNumber(String panCardNumber) {
        log.info("Fetching accounts for PAN: {}", panCardNumber);
        List<Account> accounts = accountRepository.findByPanCardNumber(panCardNumber);
        return accounts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccountById(Integer accountNumber) {
        Account acc = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        accountRepository.delete(acc);
    }

    //method to log transactions to audit service
    private void logToAuditService(Integer accountNumber, Integer receiverAccount, BigDecimal amount, String status, String transactionType) {
        try {
            Map<String, Object> auditData = new HashMap<>();
            auditData.put("paymentId", null);
            auditData.put("senderAccount", accountNumber);
            auditData.put("receiverAccount", receiverAccount);
            auditData.put("amount", amount);
            auditData.put("status", status);
            auditData.put("transactionType", transactionType);

            auditServiceClient.logTransaction(auditData);
            log.info("Transaction logged to audit service for account: {} operation: {}", accountNumber, transactionType);
        } catch (Exception e) {
            log.warn("Failed to log to audit service: {}", e.getMessage());
        }
    }

    private AccountDto convertToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setPanCardNumber(account.getPanCardNumber());
        dto.setUsername(account.getUsername());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        return dto;
    }
}
