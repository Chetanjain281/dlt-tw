package com.dlt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "audit_logs")
public class AuditLog {
    
    @Id
    private String id;
    private String orderId;
    private String stage;
    private String action;
    private String status; // SUCCESS, FAILED
    private String message;
    private LocalDateTime timestamp;
    private String details;

    public AuditLog() {
        this.timestamp = LocalDateTime.now();
    }

    public AuditLog(String orderId, String stage, String action, String status, String message) {
        this();
        this.orderId = orderId;
        this.stage = stage;
        this.action = action;
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
