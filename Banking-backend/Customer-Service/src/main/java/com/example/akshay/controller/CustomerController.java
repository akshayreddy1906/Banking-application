package com.example.akshay.controller;

import com.example.akshay.dto.CustomerDto;
import com.example.akshay.entity.Customer;
import com.example.akshay.feign.AccountServiceClient;
import com.example.akshay.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;
    private final AccountServiceClient accountServiceClient;

    @PostMapping("/")
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody Customer customer) {
        log.info("Received request to create customer : {}", customer);
        CustomerDto createdCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer contactId) {
        log.info("Received request to get customer with ID: {}", contactId);
        CustomerDto customer = customerService.getCustomerById(contactId);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Integer contactId,
             @RequestBody Customer customer) {
        log.info("Received request to update customer with ID: {}", contactId);
        CustomerDto updatedCustomer = customerService.updateCustomer(contactId, customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping("/AllCustomers")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        log.info("Received request to get all customers");
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer contactId) {
        log.info("Received request to delete customer with ID: {}", contactId);
        customerService.deleteCustomer(contactId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pan/{panCardNumber}")
    public ResponseEntity<CustomerDto> getCustomerByPanCardNumber(@PathVariable String panCardNumber) {
        log.info("Received request to get customer with PAN: {}", panCardNumber);
        CustomerDto customer = customerService.getCustomerByPanCardNumber(panCardNumber);
        return ResponseEntity.ok(customer);
    }
    

    // Feign client - get customer accounts
    @GetMapping("/{contactId}/accounts")
    public ResponseEntity<Object> getCustomerAccounts(@PathVariable Integer contactId) {
        log.info("Received request to get accounts for customer ID: {}", contactId);
        CustomerDto customer = customerService.getCustomerById(contactId);
        try {
            return accountServiceClient.getAccountsByPanCardNumber(customer.getPanCardNumber());
        } catch (Exception e) {
            log.error("Error calling Account Service: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Account Service is currently unavailable");
        }
    }
    @GetMapping("/pan/{panCardNumber}/email")
    public ResponseEntity<String> getEmailByPan(@PathVariable String panCardNumber) {
        log.info("Received request to get email for PAN: {}", panCardNumber);
        String email = customerService.getEmailByPan(panCardNumber);
        return ResponseEntity.ok(email);
    }

}