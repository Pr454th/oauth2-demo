package com.example.oauth2_demo.repository;

import com.example.oauth2_demo.entity.RoleApiAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleApiAccessRepository extends JpaRepository<RoleApiAccess, Long> {
    Optional<RoleApiAccess> findByRoleIdAndApiId(Long roleId, Long apiId);
}
