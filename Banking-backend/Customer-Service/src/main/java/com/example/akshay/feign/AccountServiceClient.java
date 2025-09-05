package com.example.akshay.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Account-Service", url = "http://localhost:8082")
public interface AccountServiceClient {

    @GetMapping("/accounts/customer/{panCardNumber}")
    ResponseEntity<Object> getAccountsByPanCardNumber(@PathVariable String panCardNumber);
}