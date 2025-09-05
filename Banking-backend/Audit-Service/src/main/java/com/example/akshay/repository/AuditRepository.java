package com.example.akshay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.akshay.entity.AuditTransaction;

@Repository
public interface AuditRepository extends JpaRepository<AuditTransaction, Long> {
   List<AuditTransaction> findAllByOrderByTimestampDesc();
   @Query("SELECT a FROM AuditTransaction a WHERE a.senderAccount = :accountId OR a.receiverAccount = :accountId ORDER BY a.timestamp DESC")
   List<AuditTransaction> findByAccountOrderByTimestampDesc(@Param("accountId") Integer accountId);

}
