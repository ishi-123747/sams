package com.sams.sams.controller;

import com.sams.sams.model.Apartment;
import com.sams.sams.model.Tenant;
import com.sams.sams.model.User;
import com.sams.sams.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TenantPortalController {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    private Tenant findTenant(String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null && user.getTenantId() != null) {
            return tenantRepository.findById(user.getTenantId()).orElse(null);
        }

        Tenant tenant = tenantRepository.findAll().stream()
                .filter(t -> t.getName() != null &&
                        t.getName().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);

        if (tenant == null) {
            tenant = tenantRepository.findAll().stream()
                    .filter(t -> t.getPhone() != null &&
                            t.getPhone().equalsIgnoreCase(username))
                    .findFirst()
                    .orElse(null);
        }

        if (tenant == null) {
            tenant = tenantRepository.findAll().stream()
                    .filter(t -> t.getEmail() != null &&
                            t.getEmail().equalsIgnoreCase(username))
                    .findFirst()
                    .orElse(null);
        }

        return tenant;
    }

    @GetMapping("/my-portal")
    public String myPortal(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null) {
            return "redirect:/login";
        }

        if ("ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/dashboard";
        }

        Tenant tenant = findTenant(username);

        if (tenant != null) {

            String aptNum = tenant.getApartmentNumber();

            Apartment apartment = aptNum != null
                    ? apartmentRepository.findAll().stream()
                    .filter(a -> a.getApartmentNumber() != null &&
                            a.getApartmentNumber().trim().equalsIgnoreCase(aptNum.trim()))
                    .findFirst()
                    .orElse(null)
                    : null;

            List<com.sams.sams.model.Payment> myPayments = aptNum != null
                    ? paymentRepository.findAll().stream()
                    .filter(p -> p.getApartmentNumber() != null &&
                            p.getApartmentNumber().trim().equalsIgnoreCase(aptNum.trim()))
                    .collect(Collectors.toList())
                    : List.of();

            List<com.sams.sams.model.Complaint> myComplaints = aptNum != null
                    ? complaintRepository.findAll().stream()
                    .filter(c -> c.getApartmentNumber() != null &&
                            c.getApartmentNumber().trim().equalsIgnoreCase(aptNum.trim()))
                    .collect(Collectors.toList())
                    : List.of();

            model.addAttribute("apartment", apartment);
            model.addAttribute("myPayments", myPayments);
            model.addAttribute("myComplaints", myComplaints);

        } else {

            model.addAttribute("apartment", null);
            model.addAttribute("myPayments", List.of());
            model.addAttribute("myComplaints", List.of());
        }

        model.addAttribute("tenant", tenant);
        model.addAttribute("activePage", "");

        return "my-portal";
    }

    @GetMapping("/my-bookings")
    public String myBookings(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
        }

        Tenant tenant = findTenant(username);

        String aptNum = tenant != null ? tenant.getApartmentNumber() : null;

        List<com.sams.sams.model.Booking> myBookings = aptNum != null
                ? bookingRepository.findAll().stream()
                .filter(b -> b.getApartmentNumber() != null &&
                        aptNum.trim().equalsIgnoreCase(b.getApartmentNumber().trim()))
                .collect(Collectors.toList())
                : List.of();

        model.addAttribute("bookings", myBookings);
        model.addAttribute("totalCount", myBookings.size());

        model.addAttribute("confirmedCount",
                myBookings.stream()
                        .filter(b -> "confirmed".equalsIgnoreCase(b.getStatus()))
                        .count());

        model.addAttribute("pendingCount",
                myBookings.stream()
                        .filter(b -> "pending".equalsIgnoreCase(b.getStatus()))
                        .count());

        model.addAttribute("completedCount",
                myBookings.stream()
                        .filter(b -> "completed".equalsIgnoreCase(b.getStatus()))
                        .count());

        model.addAttribute("activePage", "bookings");

        return "bookings";
    }

    @GetMapping("/my-bills")
    public String myBills(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
        }

        Tenant tenant = findTenant(username);

        String aptNum = tenant != null ? tenant.getApartmentNumber() : null;

        List<com.sams.sams.model.Payment> myPayments = aptNum != null
                ? paymentRepository.findAll().stream()
                .filter(p -> p.getApartmentNumber() != null &&
                        aptNum.trim().equalsIgnoreCase(p.getApartmentNumber().trim()))
                .collect(Collectors.toList())
                : List.of();

        double totalRevenue = myPayments.stream()
                .filter(p -> "paid".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(com.sams.sams.model.Payment::getAmount)
                .sum();

        model.addAttribute("payments", myPayments);
        model.addAttribute("totalRevenue", String.format("%.0f", totalRevenue));

        model.addAttribute("paidCount",
                myPayments.stream()
                        .filter(p -> "paid".equalsIgnoreCase(p.getStatus()))
                        .count());

        model.addAttribute("pendingCount",
                myPayments.stream()
                        .filter(p -> "pending".equalsIgnoreCase(p.getStatus()))
                        .count());

        model.addAttribute("overdueCount",
                myPayments.stream()
                        .filter(p -> "overdue".equalsIgnoreCase(p.getStatus()))
                        .count());

        model.addAttribute("activePage", "billing");

        return "billing";
    }

    @PostMapping("/my-portal/complaint")
    public String raiseComplaint(@RequestParam String subject,
                                 @RequestParam String description,
                                 HttpSession session) {

        String username = (String) session.getAttribute("username");

        Tenant tenant = findTenant(username);

        if (tenant != null) {

            com.sams.sams.model.Complaint complaint = new com.sams.sams.model.Complaint();

            complaint.setSubject(subject);
            complaint.setDescription(description);
            complaint.setTenantName(tenant.getName());
            complaint.setApartmentNumber(tenant.getApartmentNumber());
            complaint.setStatus("Open");
            complaint.setCategory("Other");
            complaint.setPriority("Medium");
            complaint.setCreatedDate(java.time.LocalDate.now().toString());

            complaintRepository.save(complaint);
        }

        return "redirect:/my-complaints";
    }

    @GetMapping("/my-complaints")
    public String myComplaints(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
        }

        Tenant tenant = findTenant(username);

        String aptNum = tenant != null ? tenant.getApartmentNumber() : null;

        List<com.sams.sams.model.Complaint> myComplaints = aptNum != null
                ? complaintRepository.findAll().stream()
                .filter(c -> c.getApartmentNumber() != null &&
                        aptNum.trim().equalsIgnoreCase(c.getApartmentNumber().trim()))
                .collect(Collectors.toList())
                : List.of();

        model.addAttribute("tenant", tenant);
        model.addAttribute("myComplaints", myComplaints);
        model.addAttribute("activePage", "complaints");

        return "my-complaints";
    }
}