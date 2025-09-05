package com.example.akshay.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Customer-Service", url = "http://localhost:8081")
public interface CustomerServiceClient {

    @GetMapping("/customers/pan/{panCardNumber}")
    ResponseEntity<Object> getCustomerByPanCardNumber(@PathVariable String panCardNumber);
    
}