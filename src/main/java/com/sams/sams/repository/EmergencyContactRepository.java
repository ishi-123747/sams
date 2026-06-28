package com.sams.sams.repository;

import com.sams.sams.model.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
    List<EmergencyContact> findByCategory(String category);
}