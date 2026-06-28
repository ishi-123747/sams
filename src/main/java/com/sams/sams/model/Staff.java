package com.sams.sams.model;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String role;
    private String phone;
    private String email;
    private String shift;
    private String joiningDate;
    private String status;
    private String salary;
    private String aadhar;
    private String address;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getRole() { return role; }
    public void setRole(String v) { this.role = v; }
    public String getPhone() { return phone; }
    public void setPhone(String v) { this.phone = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getShift() { return shift; }
    public void setShift(String v) { this.shift = v; }
    public String getJoiningDate() { return joiningDate; }
    public void setJoiningDate(String v) { this.joiningDate = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getSalary() { return salary; }
    public void setSalary(String v) { this.salary = v; }
    public String getAadhar() { return aadhar; }
    public void setAadhar(String v) { this.aadhar = v; }
    public String getAddress() { return address; }
    public void setAddress(String v) { this.address = v; }
}