package com.example.rabbithell.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.user.domain.model.User;

public interface UserRepository extends JpaRepository<User, String> {

    // email 기반 유저 조회
    Optional<User> findByEmail(String email);

}
