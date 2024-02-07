package com.example.userservice_rev.repositories;

import com.example.userservice_rev.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session,Long> {

    Optional<Session> findByTokenAndUser_Id(String token, Long userId);
}
