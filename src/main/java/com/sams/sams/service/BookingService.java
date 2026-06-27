package com.sams.sams.service;
import com.sams.sams.model.Booking;
import com.sams.sams.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService {
    @Autowired private BookingRepository bookingRepository;
    public List<Booking> getAllBookings() { return bookingRepository.findAll(); }
    public Booking getBookingById(Long id) { return bookingRepository.findById(id).orElse(null); }
    public void saveBooking(Booking b) { bookingRepository.save(b); }
    public void deleteBooking(Long id) { bookingRepository.deleteById(id); }
}