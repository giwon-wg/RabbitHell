package com.example.rabbithell.domain.pawcard.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.domain.pawcard.dto.request.PawCardCond;
import com.example.rabbithell.domain.pawcard.entity.PawCard;

public interface PawCardQueryRepository {
	List<PawCard> findAllByCondition(PawCardCond cond, Pageable pageable);

	long countByCondition(PawCardCond cond);
}
