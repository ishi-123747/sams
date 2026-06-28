package com.sams.sams.repository;

import com.sams.sams.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository extends JpaRepository<ParkingSlot, Long> {
    long countByStatus(String status);
}