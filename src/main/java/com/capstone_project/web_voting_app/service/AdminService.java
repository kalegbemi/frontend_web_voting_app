package com.capstone_project.web_voting_app.service;

import com.capstone_project.web_voting_app.dto.AdminRegisterRequest;
import com.capstone_project.web_voting_app.dto.AuthenticationRequest;
import com.capstone_project.web_voting_app.dto.AuthenticationResponse;
import com.capstone_project.web_voting_app.enom.Role;
import com.capstone_project.web_voting_app.model.Admin;
import com.capstone_project.web_voting_app.repository.AdminRepository;
import com.capstone_project.web_voting_app.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class AdminService {

        private final AdminRepository adminRepository;

        private final JwtService jwtService;

        private final PasswordEncoder passwordEncoder;

        private final AuthenticationManager authenticationManager;

        public Admin register(AdminRegisterRequest request){
            Admin admin = new Admin();
            admin.setEmail(request.getEmail());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setFullName(request.getFullName());
            admin.setRole(Role.ADMIN);
            adminRepository.save(admin);
            String jwtToken = jwtService.generateToken(admin);
            AuthenticationResponse.builder().token(jwtToken).build();
            return admin;
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),request.getPassword()
            ));

            Admin admin = adminRepository.findByEmail(request.getUsername())
                    .orElseThrow(()->new RuntimeException("WRONG EMAIL OR PASSWORD ENTERED"));
            adminRepository.save(admin);
            String jwtToken = jwtService.generateToken(admin);
            return AuthenticationResponse.builder().token(jwtToken).build();

        }

    }
