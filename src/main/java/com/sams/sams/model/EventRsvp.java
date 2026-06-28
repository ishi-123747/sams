package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "event_rsvps")
public class EventRsvp {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long eventId;
    private String tenantName;
    private String apartmentNumber;
    private String response; // Going, NotGoing, Maybe
    private int guestCount;
    private String respondedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long v) { this.eventId = v; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public String getResponse() { return response; }
    public void setResponse(String v) { this.response = v; }
    public int getGuestCount() { return guestCount; }
    public void setGuestCount(int v) { this.guestCount = v; }
    public String getRespondedDate() { return respondedDate; }
    public void setRespondedDate(String v) { this.respondedDate = v; }
}