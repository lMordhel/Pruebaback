package com.lulu;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "🌸 Florería Virtual - API Backend");
        response.put("status", "✅ Servidor funcionando correctamente");
        response.put("port", "8080");
        response.put("endpoints", Map.of(
            "swagger", "/swagger-ui.html",
            "api-docs", "/v3/api-docs", 
            "h2-console", "/h2-console",
            "health", "/actuator/health"
        ));
        response.put("database", "H2 (Desarrollo)");
        response.put("version", "Spring Boot 3.4.5");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", java.time.Instant.now().toString());
        return ResponseEntity.ok(health);
    }
}
