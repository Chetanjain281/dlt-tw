package com.dlt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "orders")
public class Order {
    
    @Id
    private String orderId;
    private String clientId;
    private String fundId;
    private Double requestedAmount;
    private String currentStage;
    private String overallStatus; // IN_PROGRESS, COMPLETED, FAILED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderStage> orderHistory;

    public Order() {
        this.orderHistory = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.overallStatus = "IN_PROGRESS";
    }

    public Order(String orderId, String clientId, String fundId, Double requestedAmount) {
        this();
        this.orderId = orderId;
        this.clientId = clientId;
        this.fundId = fundId;
        this.requestedAmount = requestedAmount;
        this.currentStage = "SOLICITATION";
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getFundId() { return fundId; }
    public void setFundId(String fundId) { this.fundId = fundId; }

    public Double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(Double requestedAmount) { this.requestedAmount = requestedAmount; }

    public String getCurrentStage() { return currentStage; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }

    public String getOverallStatus() { return overallStatus; }
    public void setOverallStatus(String overallStatus) { this.overallStatus = overallStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<OrderStage> getOrderHistory() { return orderHistory; }
    public void setOrderHistory(List<OrderStage> orderHistory) { this.orderHistory = orderHistory; }

    public void addStage(OrderStage stage) {
        this.orderHistory.add(stage);
        this.currentStage = stage.getStage();
        this.updatedAt = LocalDateTime.now();
    }
}
