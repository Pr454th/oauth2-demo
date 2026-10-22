package com.example.oauth2_demo.repository;

import com.example.oauth2_demo.entity.RoleApiAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleApiAccessRepository extends JpaRepository<RoleApiAccess, Long> {
}
