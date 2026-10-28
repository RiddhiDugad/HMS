package com.example.shms;

import jakarta.persistence.*;

@Entity
@Table(name="rooms")
public class Room {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(unique=true, nullable=false) private Integer roomNumber;
    private String block;
    private Integer capacity=2;
    private Integer occupied=0;
    private String status="Available";
    public Long getId(){return id;} public Integer getRoomNumber(){return roomNumber;} public void setRoomNumber(Integer v){roomNumber=v;}
    public String getBlock(){return block;} public void setBlock(String v){block=v;}
    public Integer getCapacity(){return capacity;} public void setCapacity(Integer v){capacity=v;}
    public Integer getOccupied(){return occupied;} public void setOccupied(Integer v){occupied=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
