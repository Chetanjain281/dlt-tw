package com.dlt.service;

import com.dlt.dal.JsonDataAccess;
import com.dlt.model.AuditLog;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditService {

    @Autowired
    private JsonDataAccess jsonDataAccess;

    private static final String AUDIT_LOGS_FILE = "audit_logs.json";

    public void logEvent(String orderId, String stage, String action, String status, String message) {
        try {
            List<AuditLog> auditLogs = getAllAuditLogs();
            AuditLog log = new AuditLog(orderId, stage, action, status, message);
            auditLogs.add(log);
            jsonDataAccess.writeData(AUDIT_LOGS_FILE, auditLogs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AuditLog> getOrderAuditLogs(String orderId) {
        try {
            return getAllAuditLogs().stream()
                    .filter(log -> log.getOrderId().equals(orderId))
                    .sorted((l1, l2) -> l2.getTimestamp().compareTo(l1.getTimestamp()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public List<AuditLog> getStageAuditLogs(String stage) {
        try {
            return getAllAuditLogs().stream()
                    .filter(log -> log.getStage().equals(stage))
                    .sorted((l1, l2) -> l2.getTimestamp().compareTo(l1.getTimestamp()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public List<AuditLog> getAllAuditLogs() throws IOException {
        return jsonDataAccess.readData(AUDIT_LOGS_FILE, new TypeReference<List<AuditLog>>() {});
    }
}
