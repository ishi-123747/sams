package com.sams.sams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BillingController {
    @GetMapping("/billing")
    public String billing(Model model) {
        model.addAttribute("activePage", "billing");
        return "billing";
    }
}