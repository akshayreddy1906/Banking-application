package com.example.akshay.repository;

import com.example.akshay.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findByPanCardNumber(String panCardNumber);

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByPanCardNumber(String panCardNumber);
}