package com.example.shms;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WardenRepository extends JpaRepository<Warden, Long> {
    Optional<Warden> findByUsername(String username);
    Optional<Warden> findByWardenId(String wardenId);
}
