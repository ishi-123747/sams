package com.sams.sams.controller;

import com.sams.sams.model.Payment;
import com.sams.sams.service.AuditService;
import com.sams.sams.service.PaymentService;
import com.sams.sams.service.TenantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BillingController {

    @Autowired private PaymentService paymentService;
    @Autowired private TenantService tenantService;
    @Autowired private AuditService auditService;

    @GetMapping("/billing")
    public String billing(Model model) {
        model.addAttribute("activePage", "billing");

        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);

        model.addAttribute("totalRevenue",
                payments.stream()
                        .filter(p -> "paid".equalsIgnoreCase(p.getStatus()))
                        .mapToDouble(Payment::getAmount)
                        .sum());

        model.addAttribute("paidCount",
                payments.stream()
                        .filter(p -> "paid".equalsIgnoreCase(p.getStatus()))
                        .count());

        model.addAttribute("pendingCount",
                payments.stream()
                        .filter(p -> "pending".equalsIgnoreCase(p.getStatus()))
                        .count());

        model.addAttribute("overdueCount",
                payments.stream()
                        .filter(p -> "overdue".equalsIgnoreCase(p.getStatus()))
                        .count());

        model.addAttribute("tenants", tenantService.getAllTenants());

        return "billing";
    }

    @PostMapping("/billing/add")
    public String addPayment(@ModelAttribute Payment payment,
                             HttpSession session) {

        paymentService.savePayment(payment);

        auditService.log(
                (String) session.getAttribute("username"),
                "ADD",
                "Billing",
                "Added payment of ₹" + payment.getAmount()
        );

        return "redirect:/billing";
    }

    @PostMapping("/billing/delete/{id}")
    public String deletePayment(@PathVariable Long id,
                                HttpSession session) {

        paymentService.deletePayment(id);

        auditService.log(
                (String) session.getAttribute("username"),
                "DELETE",
                "Billing",
                "Deleted payment ID: " + id
        );

        return "redirect:/billing";
    }
}