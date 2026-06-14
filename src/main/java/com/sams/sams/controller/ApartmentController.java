package com.sams.sams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApartmentController {
    @GetMapping("/apartments")
    public String apartments(Model model) {
        model.addAttribute("activePage", "apartments");
        return "apartments";
    }
}