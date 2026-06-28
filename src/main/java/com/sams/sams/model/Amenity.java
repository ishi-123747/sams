package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "amenities")
public class Amenity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String description;
    private String status;
    private int capacity;
    private String openTime;
    private String closeTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getType() { return type; }
    public void setType(String v) { this.type = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { this.description = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int v) { this.capacity = v; }
    public String getOpenTime() { return openTime; }
    public void setOpenTime(String v) { this.openTime = v; }
    public String getCloseTime() { return closeTime; }
    public void setCloseTime(String v) { this.closeTime = v; }
}