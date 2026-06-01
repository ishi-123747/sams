package com.sams.sams.controller;

import com.sams.sams.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired private ApartmentRepository apartmentRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private PaymentRepository paymentRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalApartments", apartmentRepository.count());
        model.addAttribute("totalTenants", tenantRepository.count());
        model.addAttribute("totalBookings", bookingRepository.count());
        double revenue = paymentRepository.findAll().stream()
                .filter(p -> "Paid".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(p -> p.getAmount()).sum();
        model.addAttribute("revenue", String.format("%.0f", revenue));
        model.addAttribute("recentPayments", paymentRepository.findAll().stream().limit(5).toList());
        return "dashboard";
    }
}