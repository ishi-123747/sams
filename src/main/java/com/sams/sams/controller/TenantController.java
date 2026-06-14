package com.sams.sams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TenantController {
    @GetMapping("/tenants")
    public String tenants(Model model) {
        model.addAttribute("activePage", "tenants");
        return "tenants";
    }
}