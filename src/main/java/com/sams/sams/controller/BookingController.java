package com.sams.sams.controller;

import com.sams.sams.model.Booking;
import com.sams.sams.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/bookings")
    public String bookings(Model model) {
        List<Booking> bookings = bookingRepository.findAll();
        double revenue = bookings.stream().mapToDouble(Booking::getAmount).sum();
        model.addAttribute("bookings",  bookings);
        model.addAttribute("total",     bookings.size());
        model.addAttribute("confirmed", bookings.stream().filter(b -> "Confirmed".equalsIgnoreCase(b.getStatus())).count());
        model.addAttribute("pending",   bookings.stream().filter(b -> "Pending".equalsIgnoreCase(b.getStatus())).count());
        model.addAttribute("revenue",   revenue);
        return "bookings";
    }

    @PostMapping("/bookings/add")
    public String addBooking(@RequestParam String tenantName,
                             @RequestParam String apartmentNumber,
                             @RequestParam(required=false) String checkInDate,
                             @RequestParam(required=false) String checkOutDate,
                             @RequestParam(required=false, defaultValue="0") double amount,
                             @RequestParam(required=false) String status) {
        Booking b = new Booking();
        b.setTenantName(tenantName);
        b.setApartmentNumber(apartmentNumber);
        b.setCheckInDate(checkInDate);
        b.setCheckOutDate(checkOutDate);
        b.setAmount(amount);
        b.setStatus(status != null ? status : "Pending");
        bookingRepository.save(b);
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/edit")
    public String editBooking(@RequestParam Long id,
                              @RequestParam String tenantName,
                              @RequestParam String apartmentNumber,
                              @RequestParam(required=false) String checkInDate,
                              @RequestParam(required=false) String checkOutDate,
                              @RequestParam(required=false, defaultValue="0") double amount,
                              @RequestParam(required=false) String status) {
        Booking b = bookingRepository.findById(id).orElse(new Booking());
        b.setId(id);
        b.setTenantName(tenantName);
        b.setApartmentNumber(apartmentNumber);
        b.setCheckInDate(checkInDate);
        b.setCheckOutDate(checkOutDate);
        b.setAmount(amount);
        b.setStatus(status != null ? status : "Pending");
        bookingRepository.save(b);
        return "redirect:/bookings";
    }

    @GetMapping("/bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingRepository.deleteById(id);
        return "redirect:/bookings";
    }
}