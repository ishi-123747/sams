package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apartmentName;
    private String apartmentNumber;
    private String location;
    private int rooms;
    private double rent;
    private double deposit;
    private String status;
    private String tenantName;
    private String imageUrl;

    public Apartment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getApartmentName() { return apartmentName; }
    public void setApartmentName(String v) { this.apartmentName = v; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String v) { this.apartmentNumber = v; }
    public String getLocation() { return location; }
    public void setLocation(String v) { this.location = v; }
    public int getRooms() { return rooms; }
    public void setRooms(int v) { this.rooms = v; }
    public double getRent() { return rent; }
    public void setRent(double v) { this.rent = v; }
    public double getDeposit() { return deposit; }
    public void setDeposit(double v) { this.deposit = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String v) { this.tenantName = v; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String v) { this.imageUrl = v; }
}