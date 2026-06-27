package com.sams.sams.model;
import jakarta.persistence.*;

@Entity
@Table(name = "complaints")
public class Complaint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tenantName;
    private String apartmentNumber;
    private String subject;
    @Column(length = 2000)
    private String description;
    private String category;
    private String priority;
    private String status;
    private String createdDate;
    private String resolvedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public String getSubject() { return subject; }
    public void setSubject(String v) { this.subject = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { this.description = v; }
    public String getCategory() { return category; }
    public void setCategory(String v) { this.category = v; }
    public String getPriority() { return priority; }
    public void setPriority(String v) { this.priority = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String v) { this.createdDate = v; }
    public String getResolvedDate() { return resolvedDate; }
    public void setResolvedDate(String v) { this.resolvedDate = v; }
}