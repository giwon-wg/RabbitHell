package com.example.rabbithell.domain.expedition.repository;

import static com.example.rabbithell.domain.expedition.exception.code.ExpeditionExceptionCode.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.expedition.entity.Expedition;
import com.example.rabbithell.domain.expedition.exception.ExpeditionException;

public interface ExpeditionRepository extends JpaRepository<Expedition, Long> {

	Optional<Expedition> findByUserId(Long userId);

	boolean existsByName(String name);

	default Expedition findByUserIdOrElseThrow(Long userId) {
		return findByUserId(userId)
			.orElseThrow(() -> new ExpeditionException(EXPEDITION_NOT_FOUND));
	}

}
