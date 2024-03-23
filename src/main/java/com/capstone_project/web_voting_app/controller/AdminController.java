package com.capstone_project.web_voting_app.controller;

import com.capstone_project.web_voting_app.dto.AdminRegisterRequest;
import com.capstone_project.web_voting_app.dto.AdminUpdateRequest;
import com.capstone_project.web_voting_app.dto.AuthenticationRequest;
import com.capstone_project.web_voting_app.dto.AuthenticationResponse;
import com.capstone_project.web_voting_app.model.Admin;
import com.capstone_project.web_voting_app.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
    @RequestMapping("api/v1/admin")
    public class AdminController {

    @Autowired
    public AdminService adminService;

    @SecurityRequirement(name = "bearer auth")
    @PostMapping("/register-admin")
    public ModelAndView registerAdmin(@ModelAttribute Admin admin) {
        adminService.registerAdmin(admin);
        return adminList();
    }

    @PostMapping("/admin-login")
    public ModelAndView authenticate(@ModelAttribute AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = adminService.authenticate(request);

        if (authenticationResponse.isSuccess()) {
            // If authentication is successful, redirect to admin list or another appropriate page
            return new ModelAndView("redirect:/admin-list");
        } else {
            // If authentication fails, return to the login page with an error message
            ModelAndView modelAndView = new ModelAndView("login"); // Assuming you have a login page named "login"
            modelAndView.addObject("error", "Invalid credentials");
            return modelAndView;
        }
    }

    @SecurityRequirement(name = "bearer auth")
    @GetMapping("/add-admin")
    public ModelAndView registerAdmin() {
        ModelAndView modelAndView = new ModelAndView("add-admin");
        Admin admin = new Admin();
        modelAndView.addObject("admin", admin);
        return modelAndView;
    }

    @SecurityRequirement(name = "bearer auth")
    @GetMapping(value ={"/admin-list", ""})
    public ModelAndView adminList() {
        ModelAndView modelAndView = new ModelAndView("admin-list");
        List<Admin> adminList = adminService.getAllAdmins();
        modelAndView.addObject("admins", adminList);
        return modelAndView;
    }

    @SecurityRequirement(name = "bearer auth")
    @GetMapping("/update-admin/{id}")
    public String updateAdmin(@PathVariable(value = "id") int id, Model model) {
        Admin admin = adminService.getAdminById(id);
        model.addAttribute("admin", admin);
        return "update";
    }
    @SecurityRequirement(name = "bearer auth")
    @GetMapping("/delete-admin/{id}")
    public ModelAndView deleteAdmin(@PathVariable(value = "id")  int id) {
        adminService.deleteAdmin(id);
        return adminList();

    }
}


