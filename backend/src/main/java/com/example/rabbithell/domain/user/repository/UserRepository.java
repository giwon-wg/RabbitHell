package com.example.rabbithell.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// email 기반 유저 조회
	Optional<User> findByEmail(String email);

	// 소프트 딜리트 대응
	Optional<User> findByEmailAndIsDeletedFalse(String email);

	// id 기반 조회
	Optional<User> findById(Long id);

	// 소프트 딜리트 대응
	Optional<User> findByIdAndIsDeletedFalse(Long id);
}
