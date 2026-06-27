package com.sams.sams.repository;

import com.sams.sams.model.LeaseAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaseRepository extends JpaRepository<LeaseAgreement, Long> {
    long countByStatus(String status);
}