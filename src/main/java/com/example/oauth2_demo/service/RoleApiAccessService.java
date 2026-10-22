package com.example.oauth2_demo.service;

import com.example.oauth2_demo.entity.RoleApiAccess;
import com.example.oauth2_demo.repository.RoleApiAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleApiAccessService {

    @Autowired
    private RoleApiAccessRepository roleApiAccessRepository;

    public List<RoleApiAccess> getAllRoleApiAccess() {
        return roleApiAccessRepository.findAll();
    }

    public Optional<RoleApiAccess> getRoleApiAccessById(Long id) {
        return roleApiAccessRepository.findById(id);
    }

    public RoleApiAccess createRoleApiAccess(RoleApiAccess roleApiAccess) {
        System.out.println("Creating role API access: " + roleApiAccess.getApiId()+roleApiAccess.getApiId());
        return roleApiAccessRepository.save(roleApiAccess);
    }

    public RoleApiAccess updateRoleApiAccess(Long id, RoleApiAccess roleApiAccess) {
        if (roleApiAccessRepository.existsById(id)) {
            roleApiAccess.setId(id);
            return roleApiAccessRepository.save(roleApiAccess);
        }
        return null;
    }

    public void deleteRoleApiAccess(Long id) {
        roleApiAccessRepository.deleteById(id);
    }
}
