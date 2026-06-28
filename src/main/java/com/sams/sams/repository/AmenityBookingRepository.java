package com.sams.sams.repository;

import com.sams.sams.model.AmenityBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AmenityBookingRepository extends JpaRepository<AmenityBooking, Long> {
    List<AmenityBooking> findByApartmentNumber(String apartmentNumber);
    long countByStatus(String status);
}