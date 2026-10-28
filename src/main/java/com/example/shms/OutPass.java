package com.example.shms;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="out_passes")
public class OutPass {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String studentId;
    private String studentName;
    private String reason;
    private String destination;
    private LocalDateTime departureAt;
    private LocalDateTime returnAt;
    private String status="Pending";
    public Long getId(){return id;} public String getStudentId(){return studentId;} public void setStudentId(String v){studentId=v;}
    public String getStudentName(){return studentName;} public void setStudentName(String v){studentName=v;}
    public String getReason(){return reason;} public void setReason(String v){reason=v;}
    public String getDestination(){return destination;} public void setDestination(String v){destination=v;}
    public LocalDateTime getDepartureAt(){return departureAt;} public void setDepartureAt(LocalDateTime v){departureAt=v;}
    public LocalDateTime getReturnAt(){return returnAt;} public void setReturnAt(LocalDateTime v){returnAt=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
