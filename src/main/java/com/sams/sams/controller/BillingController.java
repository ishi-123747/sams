package com.sams.sams.controller;

import com.sams.sams.model.Payment;
import com.sams.sams.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BillingController {

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/billing")
    public String billing(Model model) {
        List<Payment> payments = paymentRepository.findAll();
        long total = payments.size();
        double collected = payments.stream().filter(p -> "Paid".equalsIgnoreCase(p.getStatus())).mapToDouble(Payment::getAmount).sum();
        double pending   = payments.stream().filter(p -> "Pending".equalsIgnoreCase(p.getStatus())).mapToDouble(Payment::getAmount).sum();
        double overdue   = payments.stream().filter(p -> "Overdue".equalsIgnoreCase(p.getStatus())).mapToDouble(Payment::getAmount).sum();
        long paidCount   = payments.stream().filter(p -> "Paid".equalsIgnoreCase(p.getStatus())).count();
        double rate      = total > 0 ? (double) paidCount / total * 100 : 0;

        model.addAttribute("payments",  payments);
        model.addAttribute("collected", collected);
        model.addAttribute("pending",   pending);
        model.addAttribute("overdue",   overdue);
        model.addAttribute("rate",      String.format("%.0f", rate));
        return "billing";
    }

    @PostMapping("/billing/add")
    public String addPayment(@RequestParam String invoiceId,
                             @RequestParam String tenantName,
                             @RequestParam(required=false) String apartmentNumber,
                             @RequestParam(required=false, defaultValue="0") double amount,
                             @RequestParam(required=false) String dueDate,
                             @RequestParam(required=false) String status) {
        Payment p = new Payment();
        p.setInvoiceId(invoiceId);
        p.setTenantName(tenantName);
        p.setApartmentNumber(apartmentNumber);
        p.setAmount(amount);
        p.setDueDate(dueDate);
        p.setStatus(status != null ? status : "Pending");
        paymentRepository.save(p);
        return "redirect:/billing";
    }

    @PostMapping("/billing/edit")
    public String editPayment(@RequestParam Long id,
                              @RequestParam String invoiceId,
                              @RequestParam String tenantName,
                              @RequestParam(required=false) String apartmentNumber,
                              @RequestParam(required=false, defaultValue="0") double amount,
                              @RequestParam(required=false) String dueDate,
                              @RequestParam(required=false) String status) {
        Payment p = paymentRepository.findById(id).orElse(new Payment());
        p.setId(id);
        p.setInvoiceId(invoiceId);
        p.setTenantName(tenantName);
        p.setApartmentNumber(apartmentNumber);
        p.setAmount(amount);
        p.setDueDate(dueDate);
        p.setStatus(status != null ? status : "Pending");
        paymentRepository.save(p);
        return "redirect:/billing";
    }

    @GetMapping("/billing/delete/{id}")
    public String deletePayment(@PathVariable Long id) {
        paymentRepository.deleteById(id);
        return "redirect:/billing";
    }
}