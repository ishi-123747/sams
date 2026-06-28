package com.sams.sams.repository;

import com.sams.sams.model.DepositRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DepositRecordRepository extends JpaRepository<DepositRecord, Long> {
    List<DepositRecord> findByApartmentNumber(String apartmentNumber);
    long countByStatus(String status);
}