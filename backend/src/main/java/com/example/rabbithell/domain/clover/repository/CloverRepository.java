package com.example.rabbithell.domain.clover.repository;

import static com.example.rabbithell.domain.clover.exception.code.CloverExceptionCode.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.exception.CloverException;

public interface CloverRepository extends JpaRepository<Clover, Long> {

	Optional<Clover> findByUserId(Long userId);

	boolean existsByName(String name);

	default Clover findByUserIdOrElseThrow(Long userId) {
		return findByUserId(userId)
			.orElseThrow(() -> new CloverException(Clover_NOT_FOUND));
	}

}
