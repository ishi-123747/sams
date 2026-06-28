package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String eventDate;
    private String eventTime;
    private String venue;
    private String category;
    private String createdDate;
    private String status; // Upcoming, Completed, Cancelled

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String v) { this.title = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { this.description = v; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String v) { this.eventDate = v; }
    public String getEventTime() { return eventTime; }
    public void setEventTime(String v) { this.eventTime = v; }
    public String getVenue() { return venue; }
    public void setVenue(String v) { this.venue = v; }
    public String getCategory() { return category; }
    public void setCategory(String v) { this.category = v; }
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String v) { this.createdDate = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
}