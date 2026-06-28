package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tenant_documents")
public class TenantDocument {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tenantName;
    private String apartmentNumber;
    private String documentName;
    private String documentType;
    private String fileName;
    private String uploadDate;
    private String uploadedBy;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public String getDocumentName() { return documentName; }
    public void setDocumentName(String v) { this.documentName = v; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String v) { this.documentType = v; }
    public String getFileName() { return fileName; }
    public void setFileName(String v) { this.fileName = v; }
    public String getUploadDate() { return uploadDate; }
    public void setUploadDate(String v) { this.uploadDate = v; }
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String v) { this.uploadedBy = v; }
}