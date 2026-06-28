package com.sams.sams.repository;

import com.sams.sams.model.EventRsvp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EventRsvpRepository extends JpaRepository<EventRsvp, Long> {
    List<EventRsvp> findByEventId(Long eventId);
    long countByEventIdAndResponse(Long eventId, String response);
    Optional<EventRsvp> findByEventIdAndApartmentNumber(Long eventId, String apartmentNumber);
}