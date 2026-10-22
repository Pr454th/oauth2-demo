package com.example.oauth2_demo.controller;

import com.example.oauth2_demo.entity.Role;
import com.example.oauth2_demo.entity.User;
import com.example.oauth2_demo.repository.RoleRepository;
import com.example.oauth2_demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> getRolesUsers(@AuthenticationPrincipal OAuth2User user) {
        if (user != null) {
            System.out.println("User: " + user.getAttribute("name"));
            System.out.println("Email: " + user.getAttribute("email"));
            System.out.println("Roles: "+user.getAttribute("roles"));
        }
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
