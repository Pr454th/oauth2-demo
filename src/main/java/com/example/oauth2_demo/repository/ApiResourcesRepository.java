package com.example.oauth2_demo.repository;

import com.example.oauth2_demo.entity.ApiResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiResourcesRepository extends JpaRepository<ApiResources, Long> {
}
