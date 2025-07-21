package com.dlt.service;

import com.dlt.model.Client;
import com.dlt.model.Fund;
import com.dlt.repository.ClientRepository;
import com.dlt.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Arrays;

@Service
public class DataInitializationService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private FundRepository fundRepository;
    
    @PostConstruct
    public void initializeData() {
        initializeClients();
        initializeFunds();
    }
    
    private void initializeClients() {
        if (clientRepository.count() == 0) {
            List<Client> clients = Arrays.asList(
                new Client("CLIENT_001", "John Smith", "CONSERVATIVE", 50000.0, 45, "North America"),
                new Client("CLIENT_002", "Sarah Johnson", "MODERATE", 100000.0, 35, "Europe"),
                new Client("CLIENT_003", "Michael Brown", "AGGRESSIVE", 250000.0, 28, "Asia"),
                new Client("CLIENT_004", "Emily Davis", "CONSERVATIVE", 75000.0, 55, "North America"),
                new Client("CLIENT_005", "Robert Wilson", "MODERATE", 150000.0, 40, "Europe"),
                new Client("CLIENT_006", "Lisa Anderson", "AGGRESSIVE", 300000.0, 32, "Asia"),
                new Client("CLIENT_007", "David Taylor", "CONSERVATIVE", 60000.0, 50, "Australia"),
                new Client("CLIENT_008", "Jennifer Martinez", "MODERATE", 120000.0, 38, "South America")
            );
            clientRepository.saveAll(clients);
        }
    }
    
    private void initializeFunds() {
        if (fundRepository.count() == 0) {
            List<Fund> funds = Arrays.asList(
                new Fund("FUND_001", "Conservative Bond Fund", "BOND", 5000.0, "LOW", 4.5),
                new Fund("FUND_002", "Balanced Growth Fund", "HYBRID", 10000.0, "MEDIUM", 7.2),
                new Fund("FUND_003", "Aggressive Equity Fund", "EQUITY", 15000.0, "HIGH", 12.8),
                new Fund("FUND_004", "Global Diversified Fund", "BALANCED", 8000.0, "MEDIUM", 6.8),
                new Fund("FUND_005", "Technology Growth Fund", "EQUITY", 20000.0, "HIGH", 15.2),
                new Fund("FUND_006", "Government Securities Fund", "BOND", 3000.0, "LOW", 3.8),
                new Fund("FUND_007", "Emerging Markets Fund", "EQUITY", 25000.0, "HIGH", 18.5),
                new Fund("FUND_008", "Corporate Bond Fund", "BOND", 7500.0, "LOW", 5.2)
            );
            fundRepository.saveAll(funds);
        }
    }
    
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }
    
    public Client getClientById(String clientId) {
        return clientRepository.findById(clientId).orElse(null);
    }
    
    public Fund getFundById(String fundId) {
        return fundRepository.findById(fundId).orElse(null);
    }
}
