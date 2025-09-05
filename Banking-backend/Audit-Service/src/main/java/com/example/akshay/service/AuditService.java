package com.example.akshay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.akshay.dto.AuditLog;
import com.example.akshay.entity.AuditTransaction;
import com.example.akshay.enums.TransactionType;
import com.example.akshay.repository.AuditRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditRepository auditRepository;

    public AuditLog logTransaction(Map<String, Object> auditData) {
        log.info("Logging transaction: {}", auditData);

        AuditTransaction auditTransaction = new AuditTransaction();
        auditTransaction.setPaymentId((Integer) auditData.get("paymentId"));
        auditTransaction.setSenderAccount((Integer) auditData.get("senderAccount"));
        auditTransaction.setReceiverAccount((Integer) auditData.get("receiverAccount"));
        
        Object amountObj = auditData.get("amount");
        BigDecimal amount;
        if (amountObj instanceof Double) {
            amount = BigDecimal.valueOf((Double) amountObj);
        } else if (amountObj instanceof BigDecimal) {
            amount = (BigDecimal) amountObj;
        } else {
            amount = new BigDecimal(amountObj.toString());
        }
        auditTransaction.setAmount(amount);
        
        auditTransaction.setStatus(auditData.get("status").toString());
        auditTransaction.setTransactionType(
            TransactionType.valueOf(auditData.get("transactionType").toString())
        );

        AuditTransaction saved = auditRepository.save(auditTransaction);
        log.info("Transaction logged with audit ID: {}", saved.getAuditId());

        return convertToDto(saved);
    }
    public List<AuditLog> getAuditLogsByAccount(Integer accountId) {
        log.info("Fetching audit logs for account: {} ordered by timestamp desc", accountId);
        List<AuditTransaction> auditTransactions = auditRepository.findByAccountOrderByTimestampDesc(accountId);
        
        return auditTransactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AuditLog> getAllAuditLogs() {
        log.info("Fetching all audit logs ordered by timestamp desc");
        List<AuditTransaction> auditTransactions = auditRepository.findAllByOrderByTimestampDesc();
        
        return auditTransactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AuditLog convertToDto(AuditTransaction audit) {
        AuditLog dto = new AuditLog();
        dto.setAuditId(audit.getAuditId());
        dto.setPaymentId(audit.getPaymentId());
        dto.setSenderAccount(audit.getSenderAccount());
        dto.setReceiverAccount(audit.getReceiverAccount());
        dto.setAmount(audit.getAmount());
        dto.setStatus(audit.getStatus());
        dto.setTransactionType(audit.getTransactionType());
        dto.setTimestamp(audit.getTimestamp());
        return dto;
    }
}
