package com.sams.sams.model;
import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tenantName;
    private String apartmentNumber;
    private String checkInDate;
    private String checkOutDate;
    private String status;
    private double amount;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String v) { this.checkInDate = v; }
    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String v) { this.checkOutDate = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public double getAmount() { return amount; }
    public void setAmount(double v) { this.amount = v; }
}