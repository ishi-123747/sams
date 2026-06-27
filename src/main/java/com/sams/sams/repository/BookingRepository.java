package com.sams.sams.repository;

import com.sams.sams.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByStatus(String status);
    List<Booking> findByStatus(String status);
}
