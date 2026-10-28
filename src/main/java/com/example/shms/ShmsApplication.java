package com.example.shms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ShmsApplication {
    public static void main(String[] args) { SpringApplication.run(ShmsApplication.class, args); }

    @Bean
    CommandLineRunner seedData(StudentRepository students, RoomRepository rooms, ComplaintRepository complaints, WardenRepository wardens, AccountantRepository accountants) {
        return args -> {
            if (rooms.count() == 0) {
                for (int i = 101; i <= 120; i++) {
                    Room r = new Room();
                    r.setRoomNumber(i);
                    r.setCapacity(2);
                    r.setOccupied(0);
                    r.setBlock(i <= 110 ? "A" : "B");
                    r.setStatus("Available");
                    rooms.save(r);
                }
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (students.count() == 0) {
                Student s = new Student();
                s.setStudentId("STU1001"); s.setFullName("Aarav Sharma");
                s.setUsername("student"); s.setPasswordHash(encoder.encode("Student@123"));
                s.setEmail("aarav@example.com"); s.setPhone("9876543210"); s.setCourse("B.Tech CSE");
                s.setYearOfStudy("3rd Year"); s.setFeeStatus("Pending"); s.setApplicationStatus("Approved");
                students.save(s);
            }
            if (wardens.count() == 0) {
                Warden w = new Warden(); w.setWardenId("WRD1001"); w.setFullName("Demo Warden");
                w.setUsername("warden"); w.setPasswordHash(encoder.encode("Warden@123"));
                w.setEmail("warden@shms.local"); w.setPhone("9000000001"); w.setAssignedHostel("Block A & B"); wardens.save(w);
            }
            if (accountants.count() == 0) {
                Accountant a = new Accountant(); a.setAccountantId("ACC1001"); a.setFullName("Demo Accountant");
                a.setUsername("accountant"); a.setPasswordHash(encoder.encode("Accountant@123"));
                a.setEmail("accountant@shms.local"); a.setPhone("9000000002"); accountants.save(a);
            }
        };
    }
}
