package com.sams.sams.repository;

import com.sams.sams.model.TenantDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<TenantDocument, Long> {
    List<TenantDocument> findByApartmentNumber(String apartmentNumber);
}