package com.example.oauth2_demo.service;

import com.example.oauth2_demo.entity.ApiResources;
import com.example.oauth2_demo.entity.Role;
import com.example.oauth2_demo.entity.RoleApiAccess;
import com.example.oauth2_demo.repository.RoleApiAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class RoleApiAccessService {

    @Autowired
    private RoleApiAccessRepository roleApiAccessRepository;

    @Autowired
    @Lazy
    private CacheService cacheService;

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

    public boolean checkAccess(String roleName, String apiPath, String httpMethod) {
        Role role=cacheService.getAllRoles().stream().filter(r->r.getRoleName().equals(roleName)).findFirst().orElse(null);
        if(role==null) return false;

        ApiResources apiResource=cacheService.getAllApiResources().stream()
                .filter(a->a.getEndpoint().equals(apiPath) && a.getHttpMethod().equals(httpMethod))
                .findFirst()
                .orElse(null);
        if(apiResource==null) return false;

        System.out.println("[ ROLE ]"+role);
        System.out.println("[ API RESOURCE ]"+apiResource);
        System.out.println("[ REQUEST ]"+apiPath+httpMethod);

        RoleApiAccess roleApiAccess=cacheService.getAllRoleApiAccess()
                .stream()
                .filter(r->r.getApiId().equals(apiResource.getId()) && r.getRoleId().equals(role.getId()) && r.getAllowed().equals(1))
                .findFirst()
                .orElse(null);
        if(roleApiAccess==null) return false;

        System.out.println(roleApiAccess.getId());

        return true;
    }
}
