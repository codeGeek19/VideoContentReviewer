package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class EnvController {

    @Value("${app.environment:UNKNOWN}")
    private String environment;

    @GetMapping("/api/env")
    public Map<String, String> env() {
        return Map.of("environment", environment);
    }
}