package com.example.oauth2_demo.repository;

import com.example.oauth2_demo.entity.ApiResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiResourcesRepository extends JpaRepository<ApiResources, Long> {
    Optional<ApiResources> findByEndpointAndHttpMethod(String endpoint, String httpMethod);
}
