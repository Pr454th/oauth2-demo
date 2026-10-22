package com.example.oauth2_demo.controller;

import com.example.oauth2_demo.entity.RoleApiAccess;
import com.example.oauth2_demo.service.RoleApiAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/role-api-access")
public class RoleApiAccessController {

    @Autowired
    private RoleApiAccessService roleApiAccessService;

    @GetMapping
    public ResponseEntity<List<RoleApiAccess>> getAllRoleApiAccess() {
        return ResponseEntity.ok(roleApiAccessService.getAllRoleApiAccess());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleApiAccess> getRoleApiAccessById(@PathVariable Long id) {
        Optional<RoleApiAccess> roleApiAccess = roleApiAccessService.getRoleApiAccessById(id);
        return roleApiAccess.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoleApiAccess> createRoleApiAccess(@RequestBody RoleApiAccess roleApiAccess) {
        RoleApiAccess created = roleApiAccessService.createRoleApiAccess(roleApiAccess);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleApiAccess> updateRoleApiAccess(@PathVariable Long id, @RequestBody RoleApiAccess roleApiAccess) {
        RoleApiAccess updated = roleApiAccessService.updateRoleApiAccess(id, roleApiAccess);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleApiAccess(@PathVariable Long id) {
        roleApiAccessService.deleteRoleApiAccess(id);
        return ResponseEntity.noContent().build();
    }
}
