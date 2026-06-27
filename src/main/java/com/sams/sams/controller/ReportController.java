package com.sams.sams.controller;

import com.sams.sams.model.Apartment;
import com.sams.sams.model.Payment;
import com.sams.sams.model.Tenant;
import com.sams.sams.model.Complaint;
import com.sams.sams.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class ReportController {

    @Autowired private ApartmentRepository apartmentRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ComplaintRepository complaintRepository;

    @GetMapping("/reports")
    public String reports(Model model) {

        List<Apartment> apartments = apartmentRepository.findAll();
        List<Tenant> tenants = tenantRepository.findAll();
        List<Payment> payments = paymentRepository.findAll();
        List<Complaint> complaints = complaintRepository.findAll();

        // Count using Java stream — no custom repo method needed
        long occupiedCount = apartments.stream()
                .filter(a -> "Occupied".equalsIgnoreCase(a.getStatus())).count();
        long vacantCount = apartments.stream()
                .filter(a -> "Vacant".equalsIgnoreCase(a.getStatus())).count();
        long maintenanceCount = apartments.stream()
                .filter(a -> "Maintenance".equalsIgnoreCase(a.getStatus())).count();

        long paidCount = payments.stream()
                .filter(p -> "paid".equalsIgnoreCase(p.getStatus())).count();
        long pendingCount = payments.stream()
                .filter(p -> "pending".equalsIgnoreCase(p.getStatus())).count();
        long overdueCount = payments.stream()
                .filter(p -> "overdue".equalsIgnoreCase(p.getStatus())).count();

        long openComplaints = complaints.stream()
                .filter(c -> "Open".equalsIgnoreCase(c.getStatus())).count();

        double totalRevenue = payments.stream()
                .filter(p -> "paid".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();

        model.addAttribute("totalApartments", apartments.size());
        model.addAttribute("occupiedCount", occupiedCount);
        model.addAttribute("vacantCount", vacantCount);
        model.addAttribute("maintenanceCount", maintenanceCount);
        model.addAttribute("totalTenants", tenants.size());
        model.addAttribute("paidCount", paidCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("overdueCount", overdueCount);
        model.addAttribute("openComplaints", openComplaints);
        model.addAttribute("totalRevenue", String.format("%.0f", totalRevenue));
        model.addAttribute("activePage", "reports");

        return "reports";
    }
}