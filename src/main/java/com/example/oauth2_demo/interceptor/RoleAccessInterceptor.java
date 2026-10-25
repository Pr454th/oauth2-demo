package com.example.oauth2_demo.interceptor;

import com.example.oauth2_demo.service.RoleApiAccessService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private RoleApiAccessService roleApiAccessService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws IOException {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null
                || !auth.isAuthenticated()
                || !(auth.getPrincipal() instanceof OAuth2User user)) {
            response.sendError(401, "Authentication required");
            return false;
        }

        Object rolesObj = user.getAttribute("roles");
        String roleName = null;

        if (rolesObj instanceof List<?> roles && !roles.isEmpty()) {
            roleName = roles.get(0).toString();
        } else if (rolesObj != null) {
            roleName = rolesObj.toString();
        }

        if (roleName == null) {
            response.sendError(403, "Role not found");
            return false;
        }

        String apiPath = request.getRequestURI();
        String httpMethod = request.getMethod();

        boolean hasAccess = roleApiAccessService.checkAccess(
                roleName, apiPath, httpMethod);

        System.out.println("Intercept...");
        if (!hasAccess) {
            response.sendError(403, "Access denied");
            return false;
        }

        return true;
    }
}