package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "visitors")
public class Visitor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String visitorName;
    private String visitorPhone;
    private String purpose;
    private String apartmentNumber;
    private String tenantName;
    private String otpCode;
    private String expectedDate;
    private String expectedTime;
    private String checkInTime;
    private String checkOutTime;
    private String status; // Pending, CheckedIn, CheckedOut, Cancelled
    private String entryType; // PreApproved, WalkIn
    private String createdDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVisitorName() { return visitorName; }
    public void setVisitorName(String v) { this.visitorName = v; }
    public String getVisitorPhone() { return visitorPhone; }
    public void setVisitorPhone(String v) { this.visitorPhone = v; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String v) { this.purpose = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String v) { this.otpCode = v; }
    public String getExpectedDate() { return expectedDate; }
    public void setExpectedDate(String v) { this.expectedDate = v; }
    public String getExpectedTime() { return expectedTime; }
    public void setExpectedTime(String v) { this.expectedTime = v; }
    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String v) { this.checkInTime = v; }
    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String v) { this.checkOutTime = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getEntryType() { return entryType; }
    public void setEntryType(String v) { this.entryType = v; }
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String v) { this.createdDate = v; }
}