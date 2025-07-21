package com.dlt.service;

import com.dlt.model.AuditLog;
import com.dlt.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuditService {
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    public void logEvent(String orderId, String stage, String action, String status, String message) {
        AuditLog log = new AuditLog(orderId, stage, action, status, message);
        auditLogRepository.save(log);
    }
    
    public List<AuditLog> getOrderAuditLogs(String orderId) {
        return auditLogRepository.findByOrderIdOrderByTimestampDesc(orderId);
    }
    
    public List<AuditLog> getStageAuditLogs(String stage) {
        return auditLogRepository.findByStageOrderByTimestampDesc(stage);
    }
    
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }
}
