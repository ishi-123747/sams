package com.sams.sams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportsController {
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("activePage", "reports");
        return "reports";
    }
}