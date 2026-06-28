package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "deposit_records")
public class DepositRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tenantName;
    private String apartmentNumber;
    private double depositAmount;
    private String moveInDate;
    private String moveInCondition;
    private String moveOutDate;
    private String moveOutCondition;
    private double deductionAmount;
    private String deductionReason;
    private double refundAmount;
    private String status; // Active, MovedOut, Refunded

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public double getDepositAmount() { return depositAmount; }
    public void setDepositAmount(double v) { this.depositAmount = v; }
    public String getMoveInDate() { return moveInDate; }
    public void setMoveInDate(String v) { this.moveInDate = v; }
    public String getMoveInCondition() { return moveInCondition; }
    public void setMoveInCondition(String v) { this.moveInCondition = v; }
    public String getMoveOutDate() { return moveOutDate; }
    public void setMoveOutDate(String v) { this.moveOutDate = v; }
    public String getMoveOutCondition() { return moveOutCondition; }
    public void setMoveOutCondition(String v) { this.moveOutCondition = v; }
    public double getDeductionAmount() { return deductionAmount; }
    public void setDeductionAmount(double v) { this.deductionAmount = v; }
    public String getDeductionReason() { return deductionReason; }
    public void setDeductionReason(String v) { this.deductionReason = v; }
    public double getRefundAmount() { return refundAmount; }
    public void setRefundAmount(double v) { this.refundAmount = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
}