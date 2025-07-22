package com.dlt.service;

import com.dlt.dal.JsonDataAccess;
import com.dlt.model.Client;
import com.dlt.model.Fund;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class DataInitializationService {

    @Autowired
    private JsonDataAccess jsonDataAccess;

    private List<Client> clients;
    private List<Fund> funds;

    @PostConstruct
    public void initializeData() throws IOException {
        clients = jsonDataAccess.readData("clients.json", new TypeReference<List<Client>>() {});
        funds = jsonDataAccess.readData("funds.json", new TypeReference<List<Fund>>() {});
    }

    public List<Client> getAllClients() {
        return clients;
    }

    public List<Fund> getAllFunds() {
        return funds;
    }

    public Client getClientById(String clientId) {
        return clients.stream().filter(client -> client.getId().equals(clientId)).findFirst().orElse(null);
    }

    public Fund getFundById(String fundId) {
        return funds.stream().filter(fund -> fund.getId().equals(fundId)).findFirst().orElse(null);
    }
}
