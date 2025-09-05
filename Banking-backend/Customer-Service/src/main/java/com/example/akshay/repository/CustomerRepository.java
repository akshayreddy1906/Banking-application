package com.example.akshay.repository;

import com.example.akshay.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByPanCardNumber(String panCardNumber);

    boolean existsByEmail(String email);

    boolean existsByPanCardNumber(String panCardNumber);

    boolean existsByAadhaarCardNumber(String aadhaarCardNumber);
}