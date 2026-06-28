package com.sams.sams.controller;

import com.sams.sams.model.*;
import com.sams.sams.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class RentAgreementController {

    @Autowired private RentAgreementRepository rentAgreementRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private UserRepository userRepository;

    // Admin - list and create agreements
    @GetMapping("/agreements")
    public String agreements(Model model) {
        List<RentAgreement> all = rentAgreementRepository.findAll();
        model.addAttribute("agreements", all);
        model.addAttribute("tenants", tenantRepository.findAll());
        model.addAttribute("totalCount", all.size());
        model.addAttribute("activeCount", rentAgreementRepository.countByStatus("Active"));
        model.addAttribute("expiredCount", rentAgreementRepository.countByStatus("Expired"));
        model.addAttribute("activePage", "agreements-admin");
        return "agreements";
    }

    @PostMapping("/agreements/generate")
    public String generate(@RequestParam String tenantId,
                           @RequestParam String landlordName,
                           @RequestParam double monthlyRent,
                           @RequestParam double securityDeposit,
                           @RequestParam String leaseStartDate,
                           @RequestParam String leaseEndDate,
                           @RequestParam String paymentDueDay,
                           @RequestParam String noticePeriod,
                           @RequestParam(required = false) String specialTerms) {

        Tenant tenant = tenantRepository.findById(Long.parseLong(tenantId)).orElse(null);
        if (tenant == null) return "redirect:/agreements";

        RentAgreement a = new RentAgreement();
        a.setTenantName(tenant.getName());
        a.setApartmentNumber(tenant.getApartmentNumber());
        a.setTenantPhone(tenant.getPhone());
        a.setTenantEmail(tenant.getEmail());
        a.setLandlordName(landlordName);
        a.setMonthlyRent(monthlyRent);
        a.setSecurityDeposit(securityDeposit);
        a.setLeaseStartDate(leaseStartDate);
        a.setLeaseEndDate(leaseEndDate);
        a.setPaymentDueDay(paymentDueDay);
        a.setNoticePeriod(noticePeriod);
        a.setSpecialTerms(specialTerms);
        a.setGeneratedDate(LocalDate.now().toString());
        a.setStatus("Active");
        rentAgreementRepository.save(a);

        return "redirect:/agreements";
    }

    @GetMapping("/agreements/view/{id}")
    public String viewAgreement(@PathVariable Long id, Model model) {
        RentAgreement a = rentAgreementRepository.findById(id).orElse(null);
        model.addAttribute("a", a);
        return "agreement-document";
    }

    @PostMapping("/agreements/terminate/{id}")
    public String terminate(@PathVariable Long id) {
        RentAgreement a = rentAgreementRepository.findById(id).orElse(null);
        if (a != null) {
            a.setStatus("Terminated");
            rentAgreementRepository.save(a);
        }
        return "redirect:/agreements";
    }

    @PostMapping("/agreements/delete/{id}")
    public String delete(@PathVariable Long id) {
        rentAgreementRepository.deleteById(id);
        return "redirect:/agreements";
    }

    // Tenant - view their own agreement
    @GetMapping("/my-agreement")
    public String myAgreement(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        User user = userRepository.findByUsername(username).orElse(null);
        Tenant tenant = null;
        if (user != null && user.getTenantId() != null)
            tenant = tenantRepository.findById(user.getTenantId()).orElse(null);

        List<RentAgreement> myAgreements = tenant != null
                ? rentAgreementRepository.findByApartmentNumber(tenant.getApartmentNumber())
                : List.of();

        model.addAttribute("myAgreements", myAgreements);
        model.addAttribute("activePage", "agreements-tenant");
        return "my-agreement";
    }
}