package com.sams.sams.controller;

import com.sams.sams.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        if (userService.validateLogin(username, password)) {
            session.setAttribute("loggedInUser", username);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }
}