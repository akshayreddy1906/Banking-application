package com.example.akshay.controller;

import com.example.akshay.dto.AccountDto;
import com.example.akshay.dto.BalanceResponse;
import com.example.akshay.dto.TransactionRequest;
import com.example.akshay.entity.Account;
import com.example.akshay.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody Account account) {
        log.info("Received request to create account for PAN: {}", account.getPanCardNumber());
        AccountDto createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        log.info("Received request to get all accounts");
        List<AccountDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDto> getAccountByNumber(@PathVariable Integer accountNumber) {
        log.info("Received request to get account: {}", accountNumber);
        AccountDto account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountNumber}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Integer accountNumber,
            @Valid @RequestBody TransactionRequest request) {
        log.info("Received deposit request for account: {} amount: {}", accountNumber, request.getAmount());
        AccountDto updatedAccount = accountService.deposit(accountNumber, request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    @PutMapping("/{accountNumber}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable Integer accountNumber,
            @Valid @RequestBody TransactionRequest request) {
        log.info("Received withdraw request for account: {} amount: {}", accountNumber, request.getAmount());
        AccountDto updatedAccount = accountService.withdraw(accountNumber, request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Integer accountNumber) {
        log.info("Received balance request for account: {}", accountNumber);
        BalanceResponse balance = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/customer/{panCardNumber}")
    public ResponseEntity<List<AccountDto>> getAccountsByPanCardNumber(@PathVariable String panCardNumber) {
        log.info("Received request to get accounts for PAN: {}", panCardNumber);
        List<AccountDto> accounts = accountService.getAccountsByPanCardNumber(panCardNumber);
        return ResponseEntity.ok(accounts);
    }
    
    @DeleteMapping("/{accountNumber}")
    public void CloseAccount(@PathVariable Integer accountNumber) {
        log.info("Received request to get accounts for PAN: {}", accountNumber);
        accountService.deleteAccountById(accountNumber);
    }
}