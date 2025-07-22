package com.dlt.controller;

import com.dlt.model.AuditLog;
import com.dlt.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin(origins = "*")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/all")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        try {
            List<AuditLog> logs = auditService.getAllAuditLogs();
            return ResponseEntity.ok(logs);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<AuditLog>> getOrderAuditLogs(@PathVariable String orderId) {
        List<AuditLog> logs = auditService.getOrderAuditLogs(orderId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/stage/{stage}")
    public ResponseEntity<List<AuditLog>> getStageAuditLogs(@PathVariable String stage) {
        List<AuditLog> logs = auditService.getStageAuditLogs(stage);
        return ResponseEntity.ok(logs);
    }
}
