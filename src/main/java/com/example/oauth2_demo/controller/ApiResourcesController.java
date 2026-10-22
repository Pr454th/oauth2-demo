package com.example.oauth2_demo.controller;

import com.example.oauth2_demo.entity.ApiResources;
import com.example.oauth2_demo.service.ApiResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/api-resources")
public class ApiResourcesController {

    @Autowired
    private ApiResourcesService apiResourcesService;

    @GetMapping
    public ResponseEntity<List<ApiResources>> getAllApiResources() {
        return ResponseEntity.ok(apiResourcesService.getAllApiResources());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResources> getApiResourceById(@PathVariable Long id) {
        Optional<ApiResources> apiResource = apiResourcesService.getApiResourceById(id);
        return apiResource.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResources> createApiResource(@RequestBody ApiResources apiResource) {
        ApiResources created = apiResourcesService.createApiResource(apiResource);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResources> updateApiResource(@PathVariable Long id, @RequestBody ApiResources apiResource) {
        ApiResources updated = apiResourcesService.updateApiResource(id, apiResource);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApiResource(@PathVariable Long id) {
        apiResourcesService.deleteApiResource(id);
        return ResponseEntity.noContent().build();
    }
}
