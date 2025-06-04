package com.example.rabbithell.domain.clover.repository;

import static com.example.rabbithell.domain.clover.exception.code.CloverExceptionCode.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.exception.CloverException;

public interface CloverRepository extends JpaRepository<Clover, Long> {

	boolean existsByUserId(Long id);

	@Query("SELECT c FROM Clover c LEFT JOIN FETCH c.members WHERE c.id = :id")
	Optional<Clover> findByUserId(@Param("id") Long userId);

	boolean existsByName(String name);

	default Clover findByUserIdOrElseThrow(Long userId) {
		return findByUserId(userId)
			.orElseThrow(() -> new CloverException(CLOVER_NOT_FOUND));
	}


	default Clover findByIdOrElseThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new CloverException(CLOVER_NOT_FOUND));
	}

}
