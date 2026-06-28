package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "amenity_bookings")
public class AmenityBooking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String amenityName;
    private String tenantName;
    private String apartmentNumber;
    private String bookingDate;
    private String timeSlot;
    private String status;
    private String createdDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAmenityName() { return amenityName; }
    public void setAmenityName(String v) { this.amenityName = v; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String v) { this.bookingDate = v; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String v) { this.timeSlot = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String v) { this.createdDate = v; }
}