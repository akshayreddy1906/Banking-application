package com.example.akshay.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "Audit-Service")
public interface AuditServiceClient {

    @PostMapping("/audit/log")
    ResponseEntity logTransaction(@RequestBody Map<String, Object> auditData);
}
