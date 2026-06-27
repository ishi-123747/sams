package com.sams.sams.repository;

import com.sams.sams.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    long countByStatus(String status);
    List<Tenant> findByStatus(String status);
}