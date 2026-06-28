package com.sams.sams.repository;

import com.sams.sams.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    List<Visitor> findByApartmentNumber(String apartmentNumber);
    Optional<Visitor> findByOtpCodeAndStatus(String otpCode, String status);
    long countByStatus(String status);
    List<Visitor> findByStatusOrderByIdDesc(String status);
    List<Visitor> findAllByOrderByIdDesc();
}