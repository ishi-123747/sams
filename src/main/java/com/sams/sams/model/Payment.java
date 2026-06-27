package com.sams.sams.model;
import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invoiceId;
    private String tenantName;
    private String apartmentNumber;
    private double amount;
    private String status;
    private String dueDate;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String paidDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String v) { this.invoiceId = v; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public double getAmount() { return amount; }
    public void setAmount(double v) { this.amount = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String v) { this.dueDate = v; }
    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String v) { this.razorpayOrderId = v; }
    public String getRazorpayPaymentId() { return razorpayPaymentId; }
    public void setRazorpayPaymentId(String v) { this.razorpayPaymentId = v; }
    public String getPaidDate() { return paidDate; }
    public void setPaidDate(String v) { this.paidDate = v; }
}