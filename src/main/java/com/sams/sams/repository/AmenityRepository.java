package com.sams.sams.repository;

import com.sams.sams.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    long countByStatus(String status);
}