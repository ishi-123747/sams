package com.sams.sams.repository;

import com.sams.sams.model.MaintenanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<MaintenanceRequest, Long> {
    long countByStatus(String status);
    List<MaintenanceRequest> findByStatus(String status);
}