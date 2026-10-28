package com.example.shms;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="fee_payments")
public class FeePayment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private String studentId; private String studentName; private String feeType; private Double amount; private String paymentMode; private String receiptNumber; private LocalDateTime paidAt;
 public Long getId(){return id;} public String getStudentId(){return studentId;} public void setStudentId(String v){studentId=v;} public String getStudentName(){return studentName;} public void setStudentName(String v){studentName=v;} public String getFeeType(){return feeType;} public void setFeeType(String v){feeType=v;} public Double getAmount(){return amount;} public void setAmount(Double v){amount=v;} public String getPaymentMode(){return paymentMode;} public void setPaymentMode(String v){paymentMode=v;} public String getReceiptNumber(){return receiptNumber;} public void setReceiptNumber(String v){receiptNumber=v;} public LocalDateTime getPaidAt(){return paidAt;} public void setPaidAt(LocalDateTime v){paidAt=v;}
}
