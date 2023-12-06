package com.example.userservice.repository;

import com.example.userservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepo extends JpaRepository<Session,Long> {
    Optional<Session> findByTokenAndUserId(String token, Long id);
    //Session save(Session session);
}
