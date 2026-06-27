package com.sams.sams.controller;

import com.sams.sams.model.User;
import com.sams.sams.repository.TenantRepository;
import com.sams.sams.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SettingsController {

    @Autowired private UserRepository userRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("activePage", "settings");
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("tenants", tenantRepository.findAll());
        return "settings";
    }

    @PostMapping("/settings/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        model.addAttribute("activePage", "settings");
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            model.addAttribute("error", "User not found.");
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("tenants", tenantRepository.findAll());
            return "settings";
        }
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Current password is incorrect.");
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("tenants", tenantRepository.findAll());
            return "settings";
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("tenants", tenantRepository.findAll());
            return "settings";
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        model.addAttribute("success", "Password updated successfully!");
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("tenants", tenantRepository.findAll());
        return "settings";
    }

    @PostMapping("/settings/add-user")
    public String addUser(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String role,
                          @RequestParam(required = false) Long tenantId,
                          Model model) {

        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists.");
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("tenants", tenantRepository.findAll());
            model.addAttribute("activePage", "settings");
            return "settings";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(role);
        if ("TENANT".equalsIgnoreCase(role) && tenantId != null) {
            newUser.setTenantId(tenantId);
        }
        userRepository.save(newUser);
        model.addAttribute("success", "User created successfully!");
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("tenants", tenantRepository.findAll());
        model.addAttribute("activePage", "settings");
        return "settings";
    }

    @PostMapping("/settings/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/settings";
    }
}