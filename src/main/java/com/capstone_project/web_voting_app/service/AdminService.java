package com.capstone_project.web_voting_app.service;

import com.capstone_project.web_voting_app.dto.AdminRegisterRequest;
import com.capstone_project.web_voting_app.dto.AdminUpdateRequest;
import com.capstone_project.web_voting_app.dto.AuthenticationRequest;
import com.capstone_project.web_voting_app.dto.AuthenticationResponse;
import com.capstone_project.web_voting_app.enom.Role;
import com.capstone_project.web_voting_app.exception.AdminNotFoundException;
import com.capstone_project.web_voting_app.model.Admin;
import com.capstone_project.web_voting_app.repository.AdminRepository;
import com.capstone_project.web_voting_app.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    @RequiredArgsConstructor
    public class AdminService {

        private final AdminRepository adminRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

        @CacheEvict(value = "allAdmins", allEntries = true)
        public Admin registerAdmin (Admin admin) {
            admin.setEmail(admin.getEmail());
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setFullName(admin.getFullName());
            admin.setRole(Role.ADMIN);
            adminRepository.save(admin);
            String jwtToken = jwtService.generateToken(admin);
            AuthenticationResponse.builder().token(jwtToken).build();
            return admin;
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request){

            Admin admin = adminRepository.findByEmail(request.getUsername())
                    .orElseThrow(() -> new AdminNotFoundException("WRONG EMAIL OR PASSWORD ENTERED"));

               authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                       request.getUsername(), request.getPassword()
               ));

               //adminRepository.save(admin);
               String jwtToken = jwtService.generateToken(admin);
               return AuthenticationResponse.builder().token(jwtToken).build();

        }
        @Cacheable("allAdmins")
        public List<Admin> getAllAdmins() {
            return adminRepository.findAll();
    }
        @Cacheable(value = "singleAdmins", key = "#id")
        public Admin getAdminById(int id) {
            return adminRepository.findById(id).orElseThrow(()->new AdminNotFoundException("Admin with id: " + id + "can not be found"));
        }
        public Admin updateAdmin(int id, AdminUpdateRequest updateRequest) {

            Admin toUpdate = getAdminById(id);
            String hashedPassword = passwordEncoder.encode(updateRequest.getPassword());
            toUpdate.setPassword(hashedPassword);
            toUpdate.setFullName(updateRequest.getFullName());
            toUpdate.setEmail(updateRequest.getEmail());
            return adminRepository.save(toUpdate);
        }
        @CacheEvict(value = {"allAdmins"})
        public void deleteAdmin(int id) {
            adminRepository.deleteById(id);
        }
    }
