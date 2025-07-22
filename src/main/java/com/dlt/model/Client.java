package com.dlt.model;

public class Client {
    
    private String id;
    private String name;
    private String riskProfile; // CONSERVATIVE, MODERATE, AGGRESSIVE
    private Double investmentLimit;
    private Integer age;
    private String region;

    public Client() {}

    public Client(String id, String name, String riskProfile, 
                 Double investmentLimit, Integer age, String region) {
        this.id = id;
        this.name = name;
        this.riskProfile = riskProfile;
        this.investmentLimit = investmentLimit;
        this.age = age;
        this.region = region;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRiskProfile() { return riskProfile; }
    public void setRiskProfile(String riskProfile) { this.riskProfile = riskProfile; }

    public Double getInvestmentLimit() { return investmentLimit; }
    public void setInvestmentLimit(Double investmentLimit) { this.investmentLimit = investmentLimit; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
}
