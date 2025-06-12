package com.example.rabbithell.domain.kingdom.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.kingdom.dto.response.KingdomResponse;
import com.example.rabbithell.domain.kingdom.entity.Kingdom;
import com.example.rabbithell.domain.kingdom.repository.KingdomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KingdomServiceImpl implements KingdomService {

	private final KingdomRepository kingdomRepository;

	@Override
	@Transactional(readOnly = true)
	public KingdomResponse findKingdomById(Long id) {
		Kingdom kingdom = kingdomRepository.findByIdOrElseThrow(id);

		return KingdomResponse.from(kingdom);
	}

	@Override
	@Transactional(readOnly = true)
	public List<KingdomResponse> findAllKingdom() {

		List<Kingdom> kingdoms = kingdomRepository.findAll();

		return kingdoms.stream()
			.map(KingdomResponse::from)
			.toList();
	}
}
