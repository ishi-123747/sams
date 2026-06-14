package com.sams.sams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookingController {
    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("activePage", "bookings");
        return "bookings";
    }
}