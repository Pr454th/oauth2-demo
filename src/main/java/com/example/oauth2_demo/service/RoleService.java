package com.example.oauth2_demo.service;

import com.example.oauth2_demo.entity.Role;
import com.example.oauth2_demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService{
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public Role findByRoleName(String roleName){
        Optional<Role> role=roleRepository.findByRoleName(roleName);
        return role.orElse(null);
    }
}
