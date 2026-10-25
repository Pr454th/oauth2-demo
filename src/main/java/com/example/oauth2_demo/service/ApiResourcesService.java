package com.example.oauth2_demo.service;

import com.example.oauth2_demo.entity.ApiResources;
import com.example.oauth2_demo.repository.ApiResourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiResourcesService {

    @Autowired
    private ApiResourcesRepository apiResourcesRepository;

    public List<ApiResources> getAllApiResources() {
        return apiResourcesRepository.findAll();
    }

    public Optional<ApiResources> getApiResourceById(Long id) {
        return apiResourcesRepository.findById(id);
    }

    public ApiResources createApiResource(ApiResources apiResource) {
        return apiResourcesRepository.save(apiResource);
    }

    public ApiResources updateApiResource(Long id, ApiResources apiResource) {
        if (apiResourcesRepository.existsById(id)) {
            apiResource.setId(id);
            return apiResourcesRepository.save(apiResource);
        }
        return null;
    }

    public void deleteApiResource(Long id) {
        apiResourcesRepository.deleteById(id);
    }

    public ApiResources getApiResourceByPathAndMethod(String apiPath, String method){
        Optional<ApiResources> apiResource=apiResourcesRepository.findByEndpointAndHttpMethod(apiPath, method);
        return apiResource.orElse(null);
    }
}
