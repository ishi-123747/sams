package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tenantName;
    private String apartmentNumber;
    private int rating;
    @Column(length = 2000)
    private String comment;
    private String category;
    private String createdDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public int getRating() { return rating; }
    public void setRating(int v) { this.rating = v; }
    public String getComment() { return comment; }
    public void setComment(String v) { this.comment = v; }
    public String getCategory() { return category; }
    public void setCategory(String v) { this.category = v; }
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String v) { this.createdDate = v; }
}