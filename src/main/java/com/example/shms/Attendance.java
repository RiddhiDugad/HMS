package com.example.shms;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="attendance", uniqueConstraints=@UniqueConstraint(columnNames={"studentId","attendanceDate"}))
public class Attendance {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String studentId;
    private String studentName;
    private LocalDate attendanceDate=LocalDate.now();
    private String status="Present";
    public Long getId(){return id;} public String getStudentId(){return studentId;} public void setStudentId(String v){studentId=v;}
    public String getStudentName(){return studentName;} public void setStudentName(String v){studentName=v;}
    public LocalDate getAttendanceDate(){return attendanceDate;} public void setAttendanceDate(LocalDate v){attendanceDate=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
