package com.example.akshay.repository;

import com.example.akshay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findBySenderAccountNumberOrderByTimestampDesc(Integer senderAccountNumber);

    List<Payment> findByReceiverAccountNumberOrderByTimestampDesc(Integer receiverAccountNumber);

    List<Payment> findBySenderAccountNumberOrReceiverAccountNumberOrderByTimestampDesc(
            Integer senderAccountNumber, Integer receiverAccountNumber);
}