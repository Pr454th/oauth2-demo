package com.example.oauth2_demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexController {

    @GetMapping("/api/index")
    public Map<String, Object> index(@AuthenticationPrincipal OAuth2User user) {
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("authenticated", true);
            response.put("name", user.getAttribute("name"));
            response.put("email", user.getAttribute("email"));
            response.put("attributes", user.getAttributes());
        } else {
            response.put("authenticated", false);
        }
        return response;
    }
}
