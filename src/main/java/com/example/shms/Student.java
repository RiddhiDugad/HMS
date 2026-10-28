package com.example.shms;

import jakarta.persistence.*;

@Entity
@Table(name="students")
public class Student {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(unique=true, nullable=false) private String studentId;
    @Column(nullable=false) private String fullName;
    @Column(unique=true, nullable=false) private String username;
    @Column(nullable=false) private String passwordHash;
    private String email;
    private String phone;
    private String course;
    private String yearOfStudy;
    private Integer roomNumber;
    private String applicationStatus = "Not Applied";
    private String feeStatus = "Pending";
    public Long getId(){return id;} public String getStudentId(){return studentId;} public void setStudentId(String v){studentId=v;}
    public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getCourse(){return course;} public void setCourse(String v){course=v;}
    public String getYearOfStudy(){return yearOfStudy;} public void setYearOfStudy(String v){yearOfStudy=v;}
    public Integer getRoomNumber(){return roomNumber;} public void setRoomNumber(Integer v){roomNumber=v;}
    public String getApplicationStatus(){return applicationStatus;} public void setApplicationStatus(String v){applicationStatus=v;}
    public String getFeeStatus(){return feeStatus;} public void setFeeStatus(String v){feeStatus=v;}
}
