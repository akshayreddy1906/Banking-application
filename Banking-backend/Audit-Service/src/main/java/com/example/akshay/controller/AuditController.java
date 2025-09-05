package com.example.akshay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.akshay.dto.AuditLog;
import com.example.akshay.service.AuditService;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
@Slf4j
public class AuditController {

    private final AuditService auditService;

    @PostMapping("/log")
    public ResponseEntity<AuditLog> logTransaction(@RequestBody Map<String, Object> auditData) {
        log.info("Received audit log request: {}", auditData);
        AuditLog auditLog = auditService.logTransaction(auditData);
        return new ResponseEntity<>(auditLog, HttpStatus.CREATED);
    }
     @GetMapping("/transactions")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        log.info("Received request to get all audit logs");
        List<AuditLog> auditLogs = auditService.getAllAuditLogs();
        return ResponseEntity.ok(auditLogs);
    }

     @GetMapping("/transactions/account/{accountId}")
     public ResponseEntity<List<AuditLog>> getAuditLogsByAccount(@PathVariable Integer accountId) {
         log.info("Received request to get audit logs for account: {}", accountId);
         List<AuditLog> auditLogs = auditService.getAuditLogsByAccount(accountId);
         return ResponseEntity.ok(auditLogs);
     }


}
