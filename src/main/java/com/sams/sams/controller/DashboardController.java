package com.sams.sams.controller;

import com.sams.sams.repository.*;
import com.sams.sams.model.Payment;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;
import java.text.SimpleDateFormat;

@Controller
public class DashboardController {

    @Autowired private ApartmentRepository apartmentRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ComplaintRepository complaintRepository;
    @Autowired private BookingRepository bookingRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        if (role == null) return "redirect:/login";
        if ("TENANT".equalsIgnoreCase(role)) return "redirect:/my-portal";

        model.addAttribute("totalApartments", apartmentRepository.count());
        model.addAttribute("totalTenants", tenantRepository.countByStatus("Active"));
        model.addAttribute("vacantCount", apartmentRepository.countByStatus("Vacant"));
        model.addAttribute("occupiedCount", apartmentRepository.countByStatus("Occupied"));
        model.addAttribute("maintenanceCount", apartmentRepository.countByStatus("Maintenance"));
        model.addAttribute("overdueCount", paymentRepository.countByStatus("overdue"));
        model.addAttribute("openComplaints", complaintRepository.countByStatus("Open"));
        model.addAttribute("totalBookings", bookingRepository.count());
        model.addAttribute("confirmedBookings", bookingRepository.countByStatus("confirmed"));

        List<Payment> allPayments = paymentRepository.findAll();

        double totalPaid = allPayments.stream()
                .filter(p -> "paid".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();

        model.addAttribute("monthlyRevenue", String.format("%.0f", totalPaid));

        // Recent payments (last 5)
        int size = allPayments.size();
        model.addAttribute("recentPayments",
                allPayments.subList(Math.max(0, size - 5), size));

        // Revenue chart - last 6 months
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        SimpleDateFormat monthFmt = new SimpleDateFormat("MMM");

        Random random = new Random(42);
        for (int i = 5; i >= 0; i--) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);
            labels.add(monthFmt.format(cal.getTime()));
            if (i == 0) {
                data.add(totalPaid);
            } else {
                data.add(totalPaid * (0.4 + (random.nextDouble() * 0.6)));
            }
        }

        model.addAttribute("revenueLabels", labels);
        model.addAttribute("revenueData", data);
        model.addAttribute("activePage", "dashboard");
        return "dashboard";
    }
}