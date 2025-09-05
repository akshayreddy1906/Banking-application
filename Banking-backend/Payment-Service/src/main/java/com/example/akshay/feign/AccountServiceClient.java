package com.example.akshay.feign;

import com.example.akshay.dto.AccountDto;
import com.example.akshay.dto.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "Account-Service", url = "http://localhost:8082")
public interface AccountServiceClient {

        @GetMapping("/accounts/{accountNumber}")
        ResponseEntity<AccountDto> getAccountByNumber(@PathVariable Integer accountNumber);

        @PutMapping("/accounts/{accountNumber}/withdraw")
        ResponseEntity<AccountDto> withdraw(@PathVariable Integer accountNumber,
                        @RequestBody TransactionRequest request);

        @PutMapping("/accounts/{accountNumber}/deposit")
        ResponseEntity<AccountDto> deposit(@PathVariable Integer accountNumber,
                        @RequestBody TransactionRequest request);
}