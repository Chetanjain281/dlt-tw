package com.dlt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "funds")
public class Fund {
    
    @Id
    private String fundId;
    private String fundName;
    private String fundType; // EQUITY, BOND, HYBRID, BALANCED
    private Double minimumInvestment;
    private String riskCategory; // LOW, MEDIUM, HIGH
    private Double expectedReturn;

    public Fund() {}

    public Fund(String fundId, String fundName, String fundType, 
               Double minimumInvestment, String riskCategory, Double expectedReturn) {
        this.fundId = fundId;
        this.fundName = fundName;
        this.fundType = fundType;
        this.minimumInvestment = minimumInvestment;
        this.riskCategory = riskCategory;
        this.expectedReturn = expectedReturn;
    }

    // Getters and Setters
    public String getFundId() { return fundId; }
    public void setFundId(String fundId) { this.fundId = fundId; }

    public String getFundName() { return fundName; }
    public void setFundName(String fundName) { this.fundName = fundName; }

    public String getFundType() { return fundType; }
    public void setFundType(String fundType) { this.fundType = fundType; }

    public Double getMinimumInvestment() { return minimumInvestment; }
    public void setMinimumInvestment(Double minimumInvestment) { this.minimumInvestment = minimumInvestment; }

    public String getRiskCategory() { return riskCategory; }
    public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }

    public Double getExpectedReturn() { return expectedReturn; }
    public void setExpectedReturn(Double expectedReturn) { this.expectedReturn = expectedReturn; }
}
