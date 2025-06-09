package com.example.rabbithell.domain.pawcard.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.common.effect.enums.StatType;
import com.example.rabbithell.domain.pawcard.dto.request.CreatePawCardRequest;
import com.example.rabbithell.domain.pawcard.dto.request.PawCardCond;
import com.example.rabbithell.domain.pawcard.dto.request.UpdatePawCardRequest;
import com.example.rabbithell.domain.pawcard.dto.response.PawCardResponse;
import com.example.rabbithell.domain.pawcard.entity.PawCard;
import com.example.rabbithell.domain.pawcard.repository.PawCardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PawCardServiceImpl implements PawCardService {

	private final PawCardRepository stigmaRepository;

	@Transactional
	@Override
	public PawCardResponse createPawCard(CreatePawCardRequest request) {
		StatType statType = request.statType();
		PawCard pawCard = PawCard.builder()
			.ratioPercent(request.ratioPercent())
			.description(request.description())
			.cardNumber(request.cardNumber())
			.cardEmblem(request.cardEmblem())
			.statType(statType)
			.statCategory(statType.getCategory())
			.domainType(statType.getDomainType())
			.isDelete(false)
			.build();

		PawCard savedPawCard = stigmaRepository.save(pawCard);

		return PawCardResponse.fromEntity(pawCard);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<PawCardResponse> findAllPawCardByCond(int pageNumber, int size, PawCardCond cond) {

		Pageable pageable = PageRequest.of(pageNumber - 1, size);
		List<PawCard> pawCardList = stigmaRepository.findAllByCondition(cond, pageable);
		long count = stigmaRepository.countByCondition(cond);
		List<PawCardResponse> responseList = pawCardList.stream()
			.map(PawCardResponse::fromEntity)
			.toList();
		PageImpl<PawCardResponse> responsePage = new PageImpl<>(responseList, pageable, count);

		return PageResponse.of(responseList, responsePage);
	}

	@Transactional(readOnly = true)
	@Override
	public PawCardResponse findPawCardById(Long stigmaId) {
		PawCard pawCard = stigmaRepository.findByIdOrElseThrow(stigmaId);
		return PawCardResponse.fromEntity(pawCard);
	}

	@Transactional
	@Override
	public void updatePawCard(Long stigmaId, UpdatePawCardRequest request) {

		PawCard pawCard = stigmaRepository.findByIdOrElseThrow(stigmaId);
		StatType statType = request.statType();
		if (request.ratioPercent() != null) {
			pawCard.changeRatio(request.ratioPercent());
		}
		if (request.description() != null) {
			pawCard.changeDescription(request.description());
		}
		if (request.cardNumber() != null) {
			pawCard.changeCardNumber(request.cardNumber());
		}
		if (request.cardEmblem() != null) {
			pawCard.changeCardEmblem(request.cardEmblem());
		}
		if (statType != null) {
			pawCard.changeStatType(statType);
			pawCard.changeStatCategory(statType.getCategory());
			pawCard.changeDomainType(statType.getDomainType());
		}
	}

	@Transactional
	@Override
	public void deletePawCard(Long pawCardId) {
		PawCard pawCard = stigmaRepository.findByIdOrElseThrow(pawCardId);
		pawCard.markAsDeleted();
	}
}
