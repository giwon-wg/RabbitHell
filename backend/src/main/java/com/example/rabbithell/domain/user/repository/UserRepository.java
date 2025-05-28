package com.example.rabbithell.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // email 기반 유저 조회
    Optional<User> findByEmail(String email);
}
