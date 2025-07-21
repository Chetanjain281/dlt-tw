package com.dlt.model;

import java.time.LocalDateTime;
import java.util.Map;

public class OrderStage {
    
    private String stage;
    private LocalDateTime timestamp;
    private Map<String, Object> data;
    private String hash;
    private String status; // SUCCESS, FAILED
    private String message;

    public OrderStage() {}

    public OrderStage(String stage, Map<String, Object> data, String hash, String status) {
        this.stage = stage;
        this.timestamp = LocalDateTime.now();
        this.data = data;
        this.hash = hash;
        this.status = status;
    }

    // Getters and Setters
    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
