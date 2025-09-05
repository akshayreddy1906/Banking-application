package com.example.akshay.service;

import com.example.akshay.dto.CustomerDto;
import com.example.akshay.entity.Customer;

import java.util.List;

public interface CustomerService {

    CustomerDto createCustomer(Customer customer);

    CustomerDto getCustomerById(Integer contactId);

    CustomerDto updateCustomer(Integer contactId, Customer customer);

    List<CustomerDto> getAllCustomers();

    void deleteCustomer(Integer contactId);

    CustomerDto getCustomerByPanCardNumber(String panCardNumber);

	String getEmailByPan(String panCardNumber);
}