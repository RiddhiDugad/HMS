package com.example.shms;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="complaints")
public class Complaint {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String studentId;
    private String studentName;
    @Column(length=1500) private String description;
    private String category;
    private String status="Open";
    private LocalDateTime createdAt=LocalDateTime.now();
    public Long getId(){return id;} public String getStudentId(){return studentId;} public void setStudentId(String v){studentId=v;}
    public String getStudentName(){return studentName;} public void setStudentName(String v){studentName=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
