package com.example.akshay.service;

import com.example.akshay.dto.CustomerDto;
import com.example.akshay.entity.Customer;
import com.example.akshay.exception.CustomerAlreadyExistsException;
import com.example.akshay.exception.CustomerNotFoundException;
import com.example.akshay.feign.AccountServiceClient;
import com.example.akshay.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountServiceClient accountServiceClient;

    @Override
    public CustomerDto createCustomer(Customer customer) {
        log.info("Creating customer with email: {}", customer.getEmail());

        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer already exists with email: " + customer.getEmail());
        }
        if (customerRepository.existsByPanCardNumber(customer.getPanCardNumber())) {
            throw new CustomerAlreadyExistsException(
                    "Customer already exists with PAN: " + customer.getPanCardNumber());
        }
        if (customerRepository.existsByAadhaarCardNumber(customer.getAadhaarCardNumber())) {
            throw new CustomerAlreadyExistsException(
                    "Customer already exists with Aadhaar: " + customer.getAadhaarCardNumber());
        }

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getContactId());
        return convertToDto(savedCustomer);
    }

    @Override
    public CustomerDto getCustomerById(Integer contactId) {
        log.info("Fetching customer with ID: {}", contactId);
        Customer customer = customerRepository.findById(contactId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + contactId));
        return convertToDto(customer);
    }

    @Override
    public CustomerDto updateCustomer(Integer contactId, Customer customerUpdateData) {
        log.info("Updating customer with ID: {}", contactId);
        Customer customer = customerRepository.findById(contactId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + contactId));

        // Check email uniqueness if email is being updated and provided
        if (customerUpdateData.getEmail() != null && !customer.getEmail().equals(customerUpdateData.getEmail()) &&
                customerRepository.existsByEmail(customerUpdateData.getEmail())) {
            throw new CustomerAlreadyExistsException("Email already exists: " + customerUpdateData.getEmail());
        }

        if (customerUpdateData.getName() != null) {
            customer.setName(customerUpdateData.getName());
        }
        if (customerUpdateData.getEmail() != null) {
            customer.setEmail(customerUpdateData.getEmail());
        }
        if (customerUpdateData.getMobileNumber() != null) {
            customer.setMobileNumber(customerUpdateData.getMobileNumber());
        }
        if (customerUpdateData.getAddress() != null) {
            customer.setAddress(customerUpdateData.getAddress());
        }
        if (customerUpdateData.getAge() != null) {
            customer.setAge(customerUpdateData.getAge());
        }
        if (customerUpdateData.getGender() != null) {
            customer.setGender(customerUpdateData.getGender());
        }

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully with ID: {}", updatedCustomer.getContactId());
        return convertToDto(updatedCustomer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCustomer(Integer contactId) {
        log.info("Deleting customer with ID: {}", contactId);
        Customer customer = customerRepository.findById(contactId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + contactId));

        try {
            accountServiceClient.getAccountsByPanCardNumber(customer.getPanCardNumber());
            log.warn("Customer has associated accounts, proceeding with deletion anyway");
        } catch (Exception e) {
            log.info("No accounts found for customer ");
        }

        customerRepository.delete(customer);
        log.info("Customer deleted successfully with ID: {}", contactId);
    }

    @Override
    public CustomerDto getCustomerByPanCardNumber(String panCardNumber) {
        log.info("Fetching customer with PAN: {}", panCardNumber);
        Customer customer = customerRepository.findByPanCardNumber(panCardNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with PAN: " + panCardNumber));
        return convertToDto(customer);
    }

    @Override
    public String getEmailByPan(String panCardNumber) {
        log.info("Fetching email for PAN: {}", panCardNumber);
        Customer customer = customerRepository.findByPanCardNumber(panCardNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with PAN: " + panCardNumber));
        return customer.getEmail();
    }

    private CustomerDto convertToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setContactId(customer.getContactId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setMobileNumber(customer.getMobileNumber());
        dto.setAddress(customer.getAddress());
        dto.setAge(customer.getAge());
        dto.setGender(customer.getGender());
        dto.setPanCardNumber(customer.getPanCardNumber());
        dto.setAadhaarCardNumber(customer.getAadhaarCardNumber());
        return dto;
    }
}