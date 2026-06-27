package com.sams.sams.controller;

import com.sams.sams.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.stream.Collectors;

@Controller
public class NotificationController {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ComplaintRepository complaintRepository;
    @Autowired private ApartmentRepository apartmentRepository;

    @GetMapping("/notifications")
    public String notifications(Model model) {
        var overdue = paymentRepository.findAll().stream()
                .filter(p -> "overdue".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.toList());
        var pending = paymentRepository.findAll().stream()
                .filter(p -> "pending".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.toList());
        var openComplaints = complaintRepository.findAll().stream()
                .filter(c -> "Open".equalsIgnoreCase(c.getStatus()))
                .collect(Collectors.toList());
        var vacant = apartmentRepository.findAll().stream()
                .filter(a -> "Vacant".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());

        model.addAttribute("overduePayments", overdue);
        model.addAttribute("pendingPayments", pending);
        model.addAttribute("openComplaints", openComplaints);
        model.addAttribute("vacantApartments", vacant);
        model.addAttribute("totalAlerts",
                overdue.size() + pending.size() + openComplaints.size() + vacant.size());
        model.addAttribute("activePage", "notifications");
        return "notifications";
    }
}