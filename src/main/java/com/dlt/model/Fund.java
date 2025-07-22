package com.dlt.model;

public class Fund {
    
    private String id;
    private String name;
    private String type; // EQUITY, BOND, HYBRID, BALANCED
    private Double minimumInvestment;
    private String riskLevel; // LOW, MEDIUM, HIGH
    private Double averageReturn;

    public Fund() {}

    public Fund(String id, String name, String type, 
               Double minimumInvestment, String riskLevel, Double averageReturn) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.minimumInvestment = minimumInvestment;
        this.riskLevel = riskLevel;
        this.averageReturn = averageReturn;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getMinimumInvestment() { return minimumInvestment; }
    public void setMinimumInvestment(Double minimumInvestment) { this.minimumInvestment = minimumInvestment; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public Double getAverageReturn() { return averageReturn; }
    public void setAverageReturn(Double averageReturn) { this.averageReturn = averageReturn; }
}
