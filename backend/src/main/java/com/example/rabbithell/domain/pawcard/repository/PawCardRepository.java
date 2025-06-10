package com.example.rabbithell.domain.pawcard.repository;

import static com.example.rabbithell.domain.pawcard.exception.code.PawCardExceptionCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.pawcard.entity.PawCard;
import com.example.rabbithell.domain.pawcard.exception.PawCardException;

public interface PawCardRepository extends JpaRepository<PawCard, Long>, PawCardQueryRepository {

	default PawCard findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new PawCardException(PAWCARD_NOT_FOUND));
	}
}
