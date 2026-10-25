package com.example.oauth2_demo.service;

import com.example.oauth2_demo.entity.ApiResources;
import com.example.oauth2_demo.entity.Role;
import com.example.oauth2_demo.entity.RoleApiAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleApiAccessService roleApiAccessService;

    @Autowired
    private ApiResourcesService apiResourcesService;

    @Cacheable("roles")
    List<Role> getAllRoles(){
        return roleService.getAllRoles();
    }

    @Cacheable("roleApiAccess")
    List<RoleApiAccess> getAllRoleApiAccess(){
        return roleApiAccessService.getAllRoleApiAccess();
    }

    @Cacheable("apiResources")
    List<ApiResources> getAllApiResources(){
        return apiResourcesService.getAllApiResources();
    }

    @Scheduled(fixedDelay = 300000)
    @CacheEvict(cacheNames = {"roles", "roleApiAccess", "apiResources"}, allEntries = true)
    public void refreshCache(){
        System.out.println("[ Cache cleared ]");
    }
}
