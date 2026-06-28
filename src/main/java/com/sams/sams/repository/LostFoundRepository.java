package com.sams.sams.repository;

import com.sams.sams.model.LostFound;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LostFoundRepository extends JpaRepository<LostFound, Long> {
    long countByType(String type);
    long countByStatus(String status);
    List<LostFound> findByType(String type);
}