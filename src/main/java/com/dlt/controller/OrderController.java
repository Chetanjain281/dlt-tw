package com.dlt.controller;

import com.dlt.model.Order;
import com.dlt.model.Client;
import com.dlt.model.Fund;
import com.dlt.service.OrderProcessingService;
import com.dlt.service.DataInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderProcessingService orderProcessingService;
    
    @Autowired
    private DataInitializationService dataService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Received order creation request: " + request);
            
            String clientId = (String) request.get("clientId");
            String fundId = (String) request.get("fundId");
            Double requestedAmount = Double.valueOf(request.get("requestedAmount").toString());
            
            System.out.println("Parsed values - clientId: " + clientId + ", fundId: " + fundId + ", amount: " + requestedAmount);
            
            Order order = orderProcessingService.createOrder(clientId, fundId, requestedAmount);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderProcessingService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        Order order = orderProcessingService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{orderId}/validate")
    public ResponseEntity<Map<String, Object>> validateOrder(@PathVariable String orderId) {
        int invalidIndex = orderProcessingService.validateOrderHashChain(orderId);
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("invalidIndex", invalidIndex);
        response.put("message", invalidIndex == 0 ? "Hash chain is valid" : "Hash chain is broken at stage " + invalidIndex);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = dataService.getAllClients();
        return ResponseEntity.ok(clients);
    }
    
    @GetMapping("/funds")
    public ResponseEntity<List<Fund>> getAllFunds() {
        List<Fund> funds = dataService.getAllFunds();
        return ResponseEntity.ok(funds);
    }

    @PostMapping("/{orderId}/approve")
    public ResponseEntity<Void> advanceOrderStage(@PathVariable String orderId) {
        orderProcessingService.advanceOrderStage(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable String orderId) {
        orderProcessingService.rejectOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
