package com.sams.sams.repository;

import com.sams.sams.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatusOrderByEventDateAsc(String status);
    long countByStatus(String status);
}