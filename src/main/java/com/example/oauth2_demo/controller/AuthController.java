package com.example.oauth2_demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/user")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OAuth2User user) {
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("authenticated", true);
            response.put("name", user.getAttribute("name"));
            response.put("email", user.getAttribute("email"));
            response.put("role", user.getAttributes().getOrDefault("roles", null));
        } else {
            response.put("authenticated", false);
        }
        return response;
    }

    @GetMapping("/login-success")
    public Map<String, Object> loginSuccess(@AuthenticationPrincipal OAuth2User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("authenticated", true);
        if (user != null) {
            response.put("name", user.getAttribute("name"));
            response.put("email", user.getAttribute("email"));
        }
        return response;
    }

    @GetMapping("/login-failure")
    public Map<String, Object> loginFailure() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login failed");
        response.put("authenticated", false);
        return response;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "oauth2-demo");
        return response;
    }
}
