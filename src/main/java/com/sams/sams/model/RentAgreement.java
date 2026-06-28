package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rent_agreements")
public class RentAgreement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tenantName;
    private String apartmentNumber;
    private String tenantPhone;
    private String tenantEmail;
    private String landlordName;
    private double monthlyRent;
    private double securityDeposit;
    private String leaseStartDate;
    private String leaseEndDate;
    private String paymentDueDay;
    private String noticePeriod;
    private String specialTerms;
    private String generatedDate;
    private String status; // Draft, Active, Expired, Terminated

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public String getTenantPhone() { return tenantPhone; }
    public void setTenantPhone(String v) { this.tenantPhone = v; }
    public String getTenantEmail() { return tenantEmail; }
    public void setTenantEmail(String v) { this.tenantEmail = v; }
    public String getLandlordName() { return landlordName; }
    public void setLandlordName(String v) { this.landlordName = v; }
    public double getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(double v) { this.monthlyRent = v; }
    public double getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(double v) { this.securityDeposit = v; }
    public String getLeaseStartDate() { return leaseStartDate; }
    public void setLeaseStartDate(String v) { this.leaseStartDate = v; }
    public String getLeaseEndDate() { return leaseEndDate; }
    public void setLeaseEndDate(String v) { this.leaseEndDate = v; }
    public String getPaymentDueDay() { return paymentDueDay; }
    public void setPaymentDueDay(String v) { this.paymentDueDay = v; }
    public String getNoticePeriod() { return noticePeriod; }
    public void setNoticePeriod(String v) { this.noticePeriod = v; }
    public String getSpecialTerms() { return specialTerms; }
    public void setSpecialTerms(String v) { this.specialTerms = v; }
    public String getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(String v) { this.generatedDate = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
}