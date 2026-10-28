package com.example.shms;

import jakarta.persistence.*;

@Entity
@Table(name = "wardens")
public class Warden {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false) private String wardenId;
    @Column(nullable = false) private String fullName;
    @Column(unique = true, nullable = false) private String username;
    @Column(nullable = false) private String passwordHash;
    private String email;
    private String phone;
    private String assignedHostel;

    public Long getId() { return id; }
    public String getWardenId() { return wardenId; }
    public void setWardenId(String value) { this.wardenId = value; }
    public String getFullName() { return fullName; }
    public void setFullName(String value) { this.fullName = value; }
    public String getUsername() { return username; }
    public void setUsername(String value) { this.username = value; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String value) { this.passwordHash = value; }
    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }
    public String getPhone() { return phone; }
    public void setPhone(String value) { this.phone = value; }
    public String getAssignedHostel() { return assignedHostel; }
    public void setAssignedHostel(String value) { this.assignedHostel = value; }
}
