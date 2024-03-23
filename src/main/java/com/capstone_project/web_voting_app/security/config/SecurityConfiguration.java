package com.capstone_project.web_voting_app.security.config;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;


    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("api/v1/admin/register-admin",
                                        "api/v1/admin/add-admin", "api/v1/admin/admin-list",
                                        "api/v1/admin/update-admin/{id}", "api/v1/admin/delete-admin/{id}",
                                        "api/v1/voter/save",
                                        "api/v1/castVote",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/configuration/security",
                                        "/configuration/ui",
                                        "/v3/api-docs",
                                        "/v3/api-docs.yaml",
                                        "swagger-ui/**")
                                .permitAll()
                                .requestMatchers("api/v1/**")
                                .hasAnyAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();

    }

}
