package com.dlt.service;

import com.dlt.dal.JsonDataAccess;
import com.dlt.model.Order;
import com.dlt.model.OrderStage;
import com.dlt.model.Client;
import com.dlt.model.Fund;
import com.dlt.util.HashUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderProcessingService {

    @Autowired
    private JsonDataAccess jsonDataAccess;

    @Autowired
    private DataInitializationService dataService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private HashUtil hashUtil;

    private static final String ORDERS_FILE = "orders.json";

    public Order createOrder(String clientId, String fundId, Double requestedAmount) {
        try {
            Client client = dataService.getClientById(clientId);
            Fund fund = dataService.getFundById(fundId);

            if (client == null || fund == null) {
                throw new RuntimeException("Invalid client or fund ID");
            }

            String orderId = "ORDER_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Order order = new Order(orderId, clientId, fundId, requestedAmount);

            processSolicitation(order, client, fund);

            List<Order> orders = getAllOrders();
            orders.add(order);
            jsonDataAccess.writeData(ORDERS_FILE, orders);

            auditService.logEvent(orderId, "ORDER_CREATION", "CREATE_ORDER", "SUCCESS",
                    "Order created successfully");

            return order;
        } catch (Exception e) {
            auditService.logEvent("UNKNOWN", "ORDER_CREATION", "CREATE_ORDER", "FAILED",
                    "Order creation failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void processSolicitation(Order order, Client client, Fund fund) {
        try {
            Map<String, Object> solicitationData = new HashMap<>();
            solicitationData.put("solicitationId", "SOL_" + order.getOrderId());
            solicitationData.put("clientId", client.getId());
            solicitationData.put("clientName", client.getName());
            solicitationData.put("clientRiskProfile", client.getRiskProfile());
            solicitationData.put("fundId", fund.getId());
            solicitationData.put("fundName", fund.getName());
            solicitationData.put("fundType", fund.getType());
            solicitationData.put("requestedAmount", order.getRequestedAmount());

            String hash = hashUtil.calculateHash(hashUtil.getGenesisHash(), solicitationData);
            OrderStage stage = new OrderStage("SOLICITATION", solicitationData, hash, "SUCCESS");
            order.addStage(stage);

            auditService.logEvent(order.getOrderId(), "SOLICITATION", "PROCESS_SOLICITATION",
                    "SUCCESS", "Solicitation processed successfully");

            order.setOverallStatus("PENDING_APPROVAL");

        } catch (Exception e) {
            OrderStage failedStage = new OrderStage("SOLICITATION", new HashMap<>(), "", "FAILED");
            failedStage.setMessage("Solicitation failed: " + e.getMessage());
            order.addStage(failedStage);
            order.setOverallStatus("FAILED");

            auditService.logEvent(order.getOrderId(), "SOLICITATION", "PROCESS_SOLICITATION",
                    "FAILED", "Solicitation failed: " + e.getMessage());
        }
    }

    private void processSuitability(Order order, Client client, Fund fund) {
        try {
            boolean riskMatch = checkRiskSuitability(client.getRiskProfile(), fund.getRiskLevel());
            boolean amountSuitable = order.getRequestedAmount() >= fund.getMinimumInvestment() &&
                    order.getRequestedAmount() <= client.getInvestmentLimit();
            boolean ageSuitable = checkAgeSuitability(client.getAge(), fund.getType());

            Map<String, Object> suitabilityData = new HashMap<>();
            suitabilityData.put("riskMatch", riskMatch);
            suitabilityData.put("amountSuitable", amountSuitable);
            suitabilityData.put("ageSuitable", ageSuitable);

            String previousHash = order.getOrderHistory().get(order.getOrderHistory().size() - 1).getHash();
            String hash = hashUtil.calculateHash(previousHash, suitabilityData);
            OrderStage stage = new OrderStage("SUITABILITY", suitabilityData, hash, "SUCCESS");
            order.addStage(stage);
            order.setOverallStatus("PENDING_APPROVAL");

            auditService.logEvent(order.getOrderId(), "SUITABILITY", "PROCESS_SUITABILITY",
                    "SUCCESS", "Suitability check completed successfully");

        } catch (Exception e) {
            OrderStage failedStage = new OrderStage("SUITABILITY", new HashMap<>(), "", "FAILED");
            failedStage.setMessage("Suitability check failed: " + e.getMessage());
            order.addStage(failedStage);
            order.setOverallStatus("FAILED");

            auditService.logEvent(order.getOrderId(), "SUITABILITY", "PROCESS_SUITABILITY",
                    "FAILED", "Suitability check failed: " + e.getMessage());
        }
    }

    private void processOrderManager(Order order) {
        try {
            Map<String, Object> orderManagerData = new HashMap<>();
            orderManagerData.put("orderManagerId", "OM_" + order.getOrderId());
            orderManagerData.put("orderReceived", true);
            orderManagerData.put("forwardedToReview", true);

            String previousHash = order.getOrderHistory().get(order.getOrderHistory().size() - 1).getHash();
            String hash = hashUtil.calculateHash(previousHash, orderManagerData);
            OrderStage stage = new OrderStage("ORDER_MANAGER", orderManagerData, hash, "SUCCESS");
            order.addStage(stage);
            order.setOverallStatus("PENDING_APPROVAL");

            auditService.logEvent(order.getOrderId(), "ORDER_MANAGER", "PROCESS_ORDER_MANAGER",
                    "SUCCESS", "Order forwarded to reviewer");

        } catch (Exception e) {
            OrderStage failedStage = new OrderStage("ORDER_MANAGER", new HashMap<>(), "", "FAILED");
            failedStage.setMessage("Order manager processing failed: " + e.getMessage());
            order.addStage(failedStage);
            order.setOverallStatus("FAILED");

            auditService.logEvent(order.getOrderId(), "ORDER_MANAGER", "PROCESS_ORDER_MANAGER",
                    "FAILED", "Order manager processing failed: " + e.getMessage());
        }
    }

    private void processReviewer(Order order) {
        try {
            Map<String, Object> reviewerData = new HashMap<>();
            reviewerData.put("reviewerApproval", "APPROVED");
            reviewerData.put("reviewerId", "REV_001");
            reviewerData.put("reviewComments", "Order meets all compliance requirements");

            String previousHash = order.getOrderHistory().get(order.getOrderHistory().size() - 1).getHash();
            String hash = hashUtil.calculateHash(previousHash, reviewerData);
            OrderStage stage = new OrderStage("REVIEWER", reviewerData, hash, "SUCCESS");
            order.addStage(stage);
            order.setOverallStatus("PENDING_APPROVAL");

            auditService.logEvent(order.getOrderId(), "REVIEWER", "PROCESS_REVIEWER",
                    "SUCCESS", "Order approved by reviewer");

        } catch (Exception e) {
            OrderStage failedStage = new OrderStage("REVIEWER", new HashMap<>(), "", "FAILED");
            failedStage.setMessage("Reviewer processing failed: " + e.getMessage());
            order.addStage(failedStage);
            order.setOverallStatus("FAILED");

            auditService.logEvent(order.getOrderId(), "REVIEWER", "PROCESS_REVIEWER",
                    "FAILED", "Reviewer processing failed: " + e.getMessage());
        }
    }

    private void processOperations(Order order) {
        try {
            Map<String, Object> operationsData = new HashMap<>();
            operationsData.put("operationsApproval", "APPROVED");
            operationsData.put("operationsId", "OPS_001");
            operationsData.put("operationalChecks", "All operational requirements verified");

            String previousHash = order.getOrderHistory().get(order.getOrderHistory().size() - 1).getHash();
            String hash = hashUtil.calculateHash(previousHash, operationsData);
            OrderStage stage = new OrderStage("OPERATIONS", operationsData, hash, "SUCCESS");
            order.addStage(stage);
            order.setOverallStatus("PENDING_APPROVAL");

            auditService.logEvent(order.getOrderId(), "OPERATIONS", "PROCESS_OPERATIONS",
                    "SUCCESS", "Order approved by operations team");

        } catch (Exception e) {
            OrderStage failedStage = new OrderStage("OPERATIONS", new HashMap<>(), "", "FAILED");
            failedStage.setMessage("Operations processing failed: " + e.getMessage());
            order.addStage(failedStage);
            order.setOverallStatus("FAILED");

            auditService.logEvent(order.getOrderId(), "OPERATIONS", "PROCESS_OPERATIONS",
                    "FAILED", "Operations processing failed: " + e.getMessage());
        }
    }

    private void processProductProcessor(Order order) {
        try {
            Map<String, Object> processorData = new HashMap<>();
            processorData.put("settlementStatus", "COMPLETED");
            processorData.put("settlementId", "SETTLE_" + order.getOrderId());
            processorData.put("executionPrice", order.getRequestedAmount());

            String previousHash = order.getOrderHistory().get(order.getOrderHistory().size() - 1).getHash();
            String hash = hashUtil.calculateHash(previousHash, processorData);
            OrderStage stage = new OrderStage("PRODUCT_PROCESSOR", processorData, hash, "SUCCESS");
            order.addStage(stage);
            order.setOverallStatus("COMPLETED");

            auditService.logEvent(order.getOrderId(), "PRODUCT_PROCESSOR", "PROCESS_SETTLEMENT",
                    "SUCCESS", "Order settlement completed successfully");

        } catch (Exception e) {
            OrderStage failedStage = new OrderStage("PRODUCT_PROCESSOR", new HashMap<>(), "", "FAILED");
            failedStage.setMessage("Product processor failed: " + e.getMessage());
            order.addStage(failedStage);
            order.setOverallStatus("FAILED");

            auditService.logEvent(order.getOrderId(), "PRODUCT_PROCESSOR", "PROCESS_SETTLEMENT",
                    "FAILED", "Product processor failed: " + e.getMessage());
        }
    }

    public void advanceOrderStage(String orderId) {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (!"PENDING_APPROVAL".equals(order.getOverallStatus())) {
            throw new RuntimeException("Order is not pending approval");
        }

        String currentStage = order.getCurrentStage();
        Client client = dataService.getClientById(order.getClientId());
        Fund fund = dataService.getFundById(order.getFundId());

        switch (currentStage) {
            case "SOLICITATION":
                processOrderManager(order);
                break;
            case "ORDER_MANAGER":
                processSuitability(order, client, fund);
                break;
            case "SUITABILITY":
                processReviewer(order);
                break;
            case "REVIEWER":
                processOperations(order);
                break;
            case "OPERATIONS":
                processProductProcessor(order);
                break;
            default:
                return;
        }
        updateOrder(order);
    }

    public void rejectOrder(String orderId) {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (!"PENDING_APPROVAL".equals(order.getOverallStatus())) {
            throw new RuntimeException("Order is not pending approval");
        }

        order.setOverallStatus("FAILED");
        OrderStage failedStage = new OrderStage(order.getCurrentStage(), new HashMap<>(), "", "FAILED");
        failedStage.setMessage("Order rejected by user.");
        order.addStage(failedStage);
        updateOrder(order);

        auditService.logEvent(orderId, order.getCurrentStage(), "REJECT_ORDER", "SUCCESS",
                "Order rejected by user");
    }

    private boolean checkRiskSuitability(String clientRisk, String fundRisk) {
        return (clientRisk.equals("CONSERVATIVE") && fundRisk.equals("LOW")) ||
                (clientRisk.equals("MODERATE") && !fundRisk.equals("HIGH")) ||
                (clientRisk.equals("AGGRESSIVE"));
    }

    private boolean checkAgeSuitability(Integer age, String fundType) {
        if (age >= 55 && fundType.equals("EQUITY")) return false;
        if (age <= 25 && fundType.equals("BOND")) return false;
        return true;
    }

    public List<Order> getAllOrders() {
        try {
            return jsonDataAccess.readData(ORDERS_FILE, new TypeReference<List<Order>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public Order getOrderById(String orderId) {
        return getAllOrders().stream().filter(order -> order.getOrderId().equals(orderId)).findFirst().orElse(null);
    }

    private void updateOrder(Order order) {
        List<Order> orders = getAllOrders();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId().equals(order.getOrderId())) {
                orders.set(i, order);
                break;
            }
        }
        try {
            jsonDataAccess.writeData(ORDERS_FILE, orders);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int validateOrderHashChain(String orderId) {
        Order order = getOrderById(orderId);
        if (order == null) return -1;

        List<OrderStage> stages = order.getOrderHistory();
        if (stages.isEmpty()) return 0;

        OrderStage firstStage = stages.get(0);
        String expectedFirstHash = hashUtil.calculateHash(hashUtil.getGenesisHash(), firstStage.getData());
        if (!expectedFirstHash.equals(firstStage.getHash())) {
            return 1;
        }

        for (int i = 1; i < stages.size(); i++) {
            OrderStage currentStage = stages.get(i);
            OrderStage previousStage = stages.get(i - 1);

            String expectedHash = hashUtil.calculateHash(previousStage.getHash(), currentStage.getData());
            if (!expectedHash.equals(currentStage.getHash())) {
                return i + 1;
            }
        }

        return 0;
    }
}
