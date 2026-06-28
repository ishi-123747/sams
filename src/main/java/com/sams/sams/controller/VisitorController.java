package com.sams.sams.controller;

import com.sams.sams.model.*;
import com.sams.sams.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Controller
public class VisitorController {

    @Autowired private VisitorRepository visitorRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private UserRepository userRepository;

    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    private Tenant findTenant(String username) {
        try {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null && user.getTenantId() != null)
                return tenantRepository.findById(user.getTenantId()).orElse(null);

            Tenant t = tenantRepository.findAll().stream()
                    .filter(x -> x.getName() != null && x.getName().equalsIgnoreCase(username))
                    .findFirst().orElse(null);
            if (t != null) return t;

            t = tenantRepository.findAll().stream()
                    .filter(x -> x.getPhone() != null && x.getPhone().equalsIgnoreCase(username))
                    .findFirst().orElse(null);
            if (t != null) return t;

            return tenantRepository.findAll().stream()
                    .filter(x -> x.getEmail() != null && x.getEmail().equalsIgnoreCase(username))
                    .findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    // ===== ADMIN / SECURITY VIEW =====
    @GetMapping("/visitors")
    public String visitors(Model model) {
        List<Visitor> all = visitorRepository.findAllByOrderByIdDesc();
        model.addAttribute("visitors", all);
        model.addAttribute("totalCount", all.size());
        model.addAttribute("pendingCount", visitorRepository.countByStatus("Pending"));
        model.addAttribute("insideCount", visitorRepository.countByStatus("CheckedIn"));
        model.addAttribute("checkedOutCount", visitorRepository.countByStatus("CheckedOut"));
        model.addAttribute("activePage", "visitors-admin");
        return "visitors";
    }

    @PostMapping("/visitors/verify-otp")
    public String verifyOtp(@RequestParam String otpCode, Model model) {
        Visitor v = visitorRepository.findByOtpCodeAndStatus(otpCode, "Pending").orElse(null);
        if (v != null) {
            v.setStatus("CheckedIn");
            v.setCheckInTime(LocalDateTime.now().format(TS_FMT));
            visitorRepository.save(v);
        }
        return "redirect:/visitors";
    }

    @PostMapping("/visitors/walkin")
    public String walkIn(@RequestParam String visitorName,
                         @RequestParam String visitorPhone,
                         @RequestParam String purpose,
                         @RequestParam String apartmentNumber) {
        Visitor v = new Visitor();
        v.setVisitorName(visitorName);
        v.setVisitorPhone(visitorPhone);
        v.setPurpose(purpose);
        v.setApartmentNumber(apartmentNumber);
        v.setTenantName("—");
        v.setEntryType("WalkIn");
        v.setStatus("CheckedIn");
        v.setCheckInTime(LocalDateTime.now().format(TS_FMT));
        v.setCreatedDate(LocalDate.now().toString());
        visitorRepository.save(v);
        return "redirect:/visitors";
    }

    @PostMapping("/visitors/checkout/{id}")
    public String checkOut(@PathVariable Long id) {
        Visitor v = visitorRepository.findById(id).orElse(null);
        if (v != null) {
            v.setStatus("CheckedOut");
            v.setCheckOutTime(LocalDateTime.now().format(TS_FMT));
            visitorRepository.save(v);
        }
        return "redirect:/visitors";
    }

    @PostMapping("/visitors/cancel/{id}")
    public String cancel(@PathVariable Long id) {
        Visitor v = visitorRepository.findById(id).orElse(null);
        if (v != null) {
            v.setStatus("Cancelled");
            visitorRepository.save(v);
        }
        return "redirect:/visitors";
    }

    @PostMapping("/visitors/delete/{id}")
    public String delete(@PathVariable Long id) {
        visitorRepository.deleteById(id);
        return "redirect:/visitors";
    }

    // ===== TENANT VIEW =====
    @GetMapping("/my-visitors")
    public String myVisitors(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        Tenant tenant = findTenant(username);

        List<Visitor> myVisitors = tenant != null
                ? visitorRepository.findByApartmentNumber(tenant.getApartmentNumber())
                : List.of();

        model.addAttribute("myVisitors", myVisitors);
        model.addAttribute("tenant", tenant);
        model.addAttribute("activePage", "visitors-tenant");
        return "my-visitors";
    }

    @PostMapping("/my-visitors/preapprove")
    public String preApprove(@RequestParam String visitorName,
                             @RequestParam String visitorPhone,
                             @RequestParam String purpose,
                             @RequestParam String expectedDate,
                             @RequestParam String expectedTime,
                             HttpSession session) {
        String username = (String) session.getAttribute("username");
        Tenant tenant = findTenant(username);

        if (tenant != null) {
            Visitor v = new Visitor();
            v.setVisitorName(visitorName);
            v.setVisitorPhone(visitorPhone);
            v.setPurpose(purpose);
            v.setApartmentNumber(tenant.getApartmentNumber());
            v.setTenantName(tenant.getName());
            v.setExpectedDate(expectedDate);
            v.setExpectedTime(expectedTime);
            v.setOtpCode(generateOtp());
            v.setEntryType("PreApproved");
            v.setStatus("Pending");
            v.setCreatedDate(LocalDate.now().toString());
            visitorRepository.save(v);
        }
        return "redirect:/my-visitors";
    }

    @PostMapping("/my-visitors/cancel/{id}")
    public String tenantCancel(@PathVariable Long id) {
        Visitor v = visitorRepository.findById(id).orElse(null);
        if (v != null) {
            v.setStatus("Cancelled");
            visitorRepository.save(v);
        }
        return "redirect:/my-visitors";
    }

    private String generateOtp() {
        Random r = new Random();
        return String.format("%04d", r.nextInt(10000));
    }
}