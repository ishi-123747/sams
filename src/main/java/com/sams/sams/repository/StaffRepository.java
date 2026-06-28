package com.sams.sams.repository;

import com.sams.sams.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    long countByStatus(String status);
    long countByRole(String role);
    List<Staff> findByRole(String role);
}