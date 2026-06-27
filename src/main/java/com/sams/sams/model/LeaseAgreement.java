package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lease_agreements")
public class LeaseAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantName;
    private String apartmentNumber;
    private String startDate;
    private String endDate;
    private double rentAmount;
    private double depositAmount;
    private String status; // Active, Expired, Terminated
    private String filePath;
    private String uploadedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public double getRentAmount() { return rentAmount; }
    public void setRentAmount(double rentAmount) { this.rentAmount = rentAmount; }
    public double getDepositAmount() { return depositAmount; }
    public void setDepositAmount(double depositAmount) { this.depositAmount = depositAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getUploadedDate() { return uploadedDate; }
    public void setUploadedDate(String uploadedDate) { this.uploadedDate = uploadedDate; }
}