// Package declaration for the configuration class
package com.example.oauth2_demo.config;

// Import Spring core annotations for bean management and configuration
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// Import Spring Security configuration classes
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
// Import OAuth2 client classes for handling OAuth2 authentication
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
// Import CORS configuration classes
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

// Marks this class as a configuration class for Spring's application context
@Configuration
// Enables Spring Security web security support
@EnableWebSecurity
public class SecurityConfig {

    // Indicates that this method returns a bean to be managed by Spring container
    @Bean
    // Creates and returns an OAuth2AuthorizationRequestResolver for customizing OAuth2 authorization requests
    public OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository registrations) {
        // Creates a default resolver with client registrations and the base URI for OAuth2 authorization
        DefaultOAuth2AuthorizationRequestResolver resolver =
                new DefaultOAuth2AuthorizationRequestResolver(registrations, "/oauth2/authorization");
        // Customizes the authorization request to add "prompt=select_account" parameter
        // This forces the OAuth2 provider to show account selection screen
        resolver.setAuthorizationRequestCustomizer(request ->
                request.additionalParameters(parameters -> parameters.put("prompt", "select_account")));
        // Returns the configured resolver
        return resolver;
    }

    // Indicates that this method returns a bean to be managed by Spring container
    @Bean
    // Creates a CORS configuration source to allow cross-origin requests from frontend
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Allow specific HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow requests from frontend running on port 4200
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply CORS configuration to all paths
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Indicates that this method returns a bean to be managed by Spring container
    @Bean
    // Creates the SecurityFilterChain that defines the security configuration for the application
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   OAuth2AuthorizationRequestResolver authorizationRequestResolver) throws Exception {
        http
            // Enables CORS configuration using the corsConfigurationSource bean
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Configures authorization rules for HTTP requests
            .authorizeHttpRequests(auth -> auth
                // Permits access to these endpoints without authentication
                .requestMatchers("/", "/login", "/error", "/api/role-api-resources", "/static/**", "/*.js", "/*.css", "/favicon.ico", "/logout").permitAll()
                // Requires authentication for any other request not listed above
                .anyRequest().authenticated()
            )
            // Configures OAuth2 login functionality
            .oauth2Login(oauth2 -> oauth2
                // Sets the custom authorization request resolver for OAuth2 authorization endpoint
                .authorizationEndpoint(endpoint -> endpoint
                    .authorizationRequestResolver(authorizationRequestResolver))
                // Specifies the login page URL that triggers OAuth2 login with Azure Dev provider
                .loginPage("/oauth2/authorization/azure-dev")
                // Sets the URL to redirect to after successful login (redirect to frontend)
//                    .defaultSuccessUrl("http://localhost:4200", true)
                    .defaultSuccessUrl("/api/auth/login-success", true)
                // Sets the URL to redirect to after failed login
                .failureUrl("/api/auth/login-failure")
            )
            // Configures logout behavior
            .logout(logout -> logout
                // Sets the URL to redirect to after successful logout (redirect to frontend)
                .logoutSuccessUrl("http://localhost:4200")
                // Sets the logout endpoint URL
                .logoutUrl("/logout")
                // Deletes the JSESSIONID cookie on logout to clear session
                .deleteCookies("JSESSIONID")
            )
            // Disables CSRF protection (commonly used for API-only or stateless applications)
            .csrf(csrf -> csrf.disable());

        // Builds and returns the configured SecurityFilterChain
        return http.build();
    }
}
