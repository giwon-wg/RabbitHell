package com.example.rabbithell.domain.item.repository;

import static com.example.rabbithell.domain.item.exception.code.EffectExceptionCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.item.entity.Effect;
import com.example.rabbithell.domain.item.exception.EffectException;

public interface EffectRepository extends JpaRepository<Effect, Long> {

	default Effect findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new EffectException(NO_SUCH_EFFECT));
	}

}
