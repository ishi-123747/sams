package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_logs")
public class ChatLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String question;
    private String matchedCategory;
    private String timestamp;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String v) { this.username = v; }
    public String getQuestion() { return question; }
    public void setQuestion(String v) { this.question = v; }
    public String getMatchedCategory() { return matchedCategory; }
    public void setMatchedCategory(String v) { this.matchedCategory = v; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String v) { this.timestamp = v; }
}