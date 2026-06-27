package com.sams.sams.repository;

import com.sams.sams.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    long countByStatus(String status);
    List<Complaint> findByStatus(String status);
}