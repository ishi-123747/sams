package com.sams.sams.repository;

import com.sams.sams.model.RentAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RentAgreementRepository extends JpaRepository<RentAgreement, Long> {
    List<RentAgreement> findByApartmentNumber(String apartmentNumber);
    long countByStatus(String status);
}