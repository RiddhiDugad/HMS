package com.example.shms;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface AccountantRepository extends JpaRepository<Accountant,Long> { Optional<Accountant> findByUsername(String username); Optional<Accountant> findByAccountantId(String accountantId); }
