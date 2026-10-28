package com.example.shms;
import jakarta.persistence.*;
@Entity @Table(name="accountants")
public class Accountant {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(unique=true, nullable=false) private String accountantId;
 @Column(nullable=false) private String fullName;
 @Column(unique=true, nullable=false) private String username;
 @Column(nullable=false) private String passwordHash;
 private String email; private String phone;
 public Long getId(){return id;} public String getAccountantId(){return accountantId;} public void setAccountantId(String v){accountantId=v;}
 public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;} public String getUsername(){return username;} public void setUsername(String v){username=v;}
 public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;} public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
}
