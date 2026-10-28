package com.example.shms;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface StudentRepository extends JpaRepository<Student,Long> {
 Optional<Student> findByUsername(String username);
 Optional<Student> findByStudentId(String studentId);
}
