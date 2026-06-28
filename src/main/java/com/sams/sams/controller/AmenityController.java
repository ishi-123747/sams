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
public class AmenityController {

    @Autowired private AmenityRepository amenityRepository;
    @Autowired private AmenityBookingRepository amenityBookingRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private UserRepository userRepository;

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

    @GetMapping("/amenities")
    public String amenities(Model model) {
        List<Amenity> amenities = amenityRepository.findAll();
        List<AmenityBooking> bookings = amenityBookingRepository.findAll();
        model.addAttribute("amenities", amenities);
        model.addAttribute("bookings", bookings);
        model.addAttribute("totalAmenities", amenities.size());
        model.addAttribute("activeAmenities", amenityRepository.countByStatus("Active"));
        model.addAttribute("totalBookings", bookings.size());
        model.addAttribute("pendingBookings", amenityBookingRepository.countByStatus("Pending"));
        model.addAttribute("activePage", "amenities-page");
        return "amenities";
    }

    @PostMapping("/amenities/add")
    public String addAmenity(@ModelAttribute Amenity amenity) {
        if (amenity.getStatus() == null || amenity.getStatus().isEmpty()) amenity.setStatus("Active");
        amenityRepository.save(amenity);
        return "redirect:/amenities";
    }

    @PostMapping("/amenities/delete/{id}")
    public String deleteAmenity(@PathVariable Long id) {
        amenityRepository.deleteById(id);
        return "redirect:/amenities";
    }

    @PostMapping("/amenities/booking/approve/{id}")
    public String approveBooking(@PathVariable Long id) {
        AmenityBooking b = amenityBookingRepository.findById(id).orElse(null);
        if (b != null) { b.setStatus("Approved"); amenityBookingRepository.save(b); }
        return "redirect:/amenities";
    }

    @PostMapping("/amenities/booking/reject/{id}")
    public String rejectBooking(@PathVariable Long id) {
        AmenityBooking b = amenityBookingRepository.findById(id).orElse(null);
        if (b != null) { b.setStatus("Rejected"); amenityBookingRepository.save(b); }
        return "redirect:/amenities";
    }

    @PostMapping("/amenities/booking/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        amenityBookingRepository.deleteById(id);
        return "redirect:/amenities";
    }

    @GetMapping("/my-amenities")
    public String myAmenities(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        Tenant tenant = findTenant(username);

        List<AmenityBooking> myBookings = tenant != null
                ? amenityBookingRepository.findByApartmentNumber(tenant.getApartmentNumber())
                : List.of();

        model.addAttribute("amenities", amenityRepository.findAll());
        model.addAttribute("myBookings", myBookings);
        model.addAttribute("tenant", tenant);
        model.addAttribute("activePage", "amenities-tenant");
        return "my-amenities";
    }

    @PostMapping("/my-amenities/book")
    public String bookAmenity(@RequestParam String amenityName,
                              @RequestParam String bookingDate,
                              @RequestParam String timeSlot,
                              HttpSession session) {
        String username = (String) session.getAttribute("username");
        Tenant tenant = findTenant(username);

        if (tenant != null) {
            AmenityBooking booking = new AmenityBooking();
            booking.setAmenityName(amenityName);
            booking.setTenantName(tenant.getName());
            booking.setApartmentNumber(tenant.getApartmentNumber());
            booking.setBookingDate(bookingDate);
            booking.setTimeSlot(timeSlot);
            booking.setStatus("Pending");
            booking.setCreatedDate(LocalDate.now().toString());
            amenityBookingRepository.save(booking);
        }
        return "redirect:/my-amenities";
    }
}