package com.dlt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }
    
    @GetMapping("/audit")
    public String audit() {
        return "audit";
    }
    
    @GetMapping("/validation")
    public String validation() {
        return "validation";
    }
}
