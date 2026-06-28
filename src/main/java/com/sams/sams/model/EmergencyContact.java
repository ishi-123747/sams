package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "emergency_contacts")
public class EmergencyContact {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private String phone;
    private String altPhone;
    private String description;
    private String availability;
    private String priority;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getCategory() { return category; }
    public void setCategory(String v) { this.category = v; }
    public String getPhone() { return phone; }
    public void setPhone(String v) { this.phone = v; }
    public String getAltPhone() { return altPhone; }
    public void setAltPhone(String v) { this.altPhone = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { this.description = v; }
    public String getAvailability() { return availability; }
    public void setAvailability(String v) { this.availability = v; }
    public String getPriority() { return priority; }
    public void setPriority(String v) { this.priority = v; }
}