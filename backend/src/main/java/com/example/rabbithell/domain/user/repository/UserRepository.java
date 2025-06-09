package com.example.rabbithell.domain.user.repository;

import static com.example.rabbithell.domain.auth.exception.code.AuthExceptionCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.auth.exception.AuthException;

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

	default User findByIdOrElseThrow(Long id) {
		return findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new AuthException(USER_NOT_FOUND));
	}

	default User findByEmailOrElseThrow(String email) {
		return findByEmailAndIsDeletedFalse(email)
			.orElseThrow(() -> new AuthException(USER_NOT_FOUND));
	}

	List<User> id(Long id);
}
