package com.example.shms;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
 Optional<Attendance> findByStudentIdAndAttendanceDate(String studentId, LocalDate date);
}
