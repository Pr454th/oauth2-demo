package com.example.oauth2_demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_api_access")
public class RoleApiAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name="role_id")
    private Long roleId;

    @Column(name = "api_id")
    private Long apiId;

    @Column(name="allowed")
    private Integer allowed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Integer getAllowed() {
        return allowed;
    }

    public void setAllowed(Integer allowed) {
        this.allowed = allowed;
    }
}
