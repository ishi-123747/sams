package com.sams.sams.repository;

import com.sams.sams.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    long countByStatus(String status);
    List<Payment> findByStatus(String status);
    Payment findByRazorpayOrderId(String razorpayOrderId);
}
