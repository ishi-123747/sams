package com.sams.sams.controller;

import com.sams.sams.repository.PaymentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/billing/invoice/{id}")
    public String printInvoice(@PathVariable Long id, Model model) {
        paymentRepository.findById(id).ifPresent(p -> model.addAttribute("payment", p));
        return "invoice-print";
    }

    @GetMapping("/payments")
    public String payments() {
        return "redirect:/billing";
    }
}