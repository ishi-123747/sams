package com.sams.sams.controller;

import com.sams.sams.model.User;
import com.sams.sams.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String root(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if ("TENANT".equalsIgnoreCase(role)) return "redirect:/my-portal";
        if ("ADMIN".equalsIgnoreCase(role)) return "redirect:/dashboard";
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || !user.getPassword().equals(password))  {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());
        session.setAttribute("userId", user.getId());
        if ("ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/dashboard";
        if ("TENANT".equalsIgnoreCase(user.getRole())) return "redirect:/my-portal";
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}