package com.example.shms;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="visitors")
public class Visitor {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String visitorName;
    private String studentId;
    private String purpose;
    private LocalDateTime entryTime=LocalDateTime.now();
    private LocalDateTime exitTime;
    private String status="Inside";
    public Long getId(){return id;} public String getVisitorName(){return visitorName;} public void setVisitorName(String v){visitorName=v;}
    public String getStudentId(){return studentId;} public void setStudentId(String v){studentId=v;}
    public String getPurpose(){return purpose;} public void setPurpose(String v){purpose=v;}
    public LocalDateTime getEntryTime(){return entryTime;} public void setEntryTime(LocalDateTime v){entryTime=v;}
    public LocalDateTime getExitTime(){return exitTime;} public void setExitTime(LocalDateTime v){exitTime=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
