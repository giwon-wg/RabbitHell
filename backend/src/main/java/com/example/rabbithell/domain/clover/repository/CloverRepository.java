package com.example.rabbithell.domain.clover.repository;

import static com.example.rabbithell.domain.clover.exception.code.CloverExceptionCode.*;
import static com.example.rabbithell.domain.clover.exception.code.CloverExceptionCode.DUPLICATED_CLOVER_NAME;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.exception.CloverException;

public interface CloverRepository extends JpaRepository<Clover, Long> {

	Optional<Clover> findByUserId(Long userId);

	boolean existsByName(String name);

	default Clover findByUserIdOrElseThrow(Long userId) {
		return findByUserId(userId)
			.orElseThrow(() -> new CloverException(CLOVER_NOT_FOUND));
	}

	default void validateNameIsUnique(String name) {
		if (existsByName(name)) {
			throw new CloverException(DUPLICATED_CLOVER_NAME);
		}
	}

}
