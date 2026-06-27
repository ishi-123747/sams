package com.sams.sams.repository;

import com.sams.sams.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    long countByStatus(String status);
    List<Apartment> findByStatus(String status);
}