package com.dlt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
public class Client {
    
    @Id
    private String clientId;
    private String clientName;
    private String riskProfile; // CONSERVATIVE, MODERATE, AGGRESSIVE
    private Double investmentCapacity;
    private Integer age;
    private String region;

    public Client() {}

    public Client(String clientId, String clientName, String riskProfile, 
                 Double investmentCapacity, Integer age, String region) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.riskProfile = riskProfile;
        this.investmentCapacity = investmentCapacity;
        this.age = age;
        this.region = region;
    }

    // Getters and Setters
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getRiskProfile() { return riskProfile; }
    public void setRiskProfile(String riskProfile) { this.riskProfile = riskProfile; }

    public Double getInvestmentCapacity() { return investmentCapacity; }
    public void setInvestmentCapacity(Double investmentCapacity) { this.investmentCapacity = investmentCapacity; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
}
