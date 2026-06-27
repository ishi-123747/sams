package com.sams.sams.controller;
import com.sams.sams.model.Booking;
import com.sams.sams.service.BookingService;
import com.sams.sams.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class BookingController {
    @Autowired private BookingService bookingService;
    @Autowired private ApartmentService apartmentService;

    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("activePage", "bookings");
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        model.addAttribute("totalCount", bookings.size());
        model.addAttribute("confirmedCount", bookings.stream().filter(b -> "confirmed".equalsIgnoreCase(b.getStatus())).count());
        model.addAttribute("pendingCount", bookings.stream().filter(b -> "pending".equalsIgnoreCase(b.getStatus())).count());
        model.addAttribute("completedCount", bookings.stream().filter(b -> "completed".equalsIgnoreCase(b.getStatus())).count());
        model.addAttribute("apartments", apartmentService.getAllApartments());
        return "bookings";
    }

    @PostMapping("/bookings/add")
    public String addBooking(@ModelAttribute Booking booking) {
        bookingService.saveBooking(booking); return "redirect:/bookings";
    }

    @PostMapping("/bookings/edit")
    public String editBooking(@ModelAttribute Booking booking) {
        Booking existing = bookingService.getBookingById(booking.getId());
        if (existing != null) {
            existing.setTenantName(booking.getTenantName());
            existing.setApartmentNumber(booking.getApartmentNumber());
            existing.setCheckInDate(booking.getCheckInDate());
            existing.setCheckOutDate(booking.getCheckOutDate());
            existing.setStatus(booking.getStatus());
            existing.setAmount(booking.getAmount());
            bookingService.saveBooking(existing);
        }
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id); return "redirect:/bookings";
    }
}