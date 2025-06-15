package com.example.rabbithell.domain.pawcard.service;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.pawcard.dto.request.CreatePawCardRequest;
import com.example.rabbithell.domain.pawcard.dto.request.PawCardCond;
import com.example.rabbithell.domain.pawcard.dto.request.UpdatePawCardRequest;
import com.example.rabbithell.domain.pawcard.dto.response.PawCardResponse;

public interface PawCardService {

	PawCardResponse createPawCard(CreatePawCardRequest request);

	PageResponse<PawCardResponse> findAllPawCardByCond(int pageNumber, int size, PawCardCond cond);

	PawCardResponse findPawCardById(Long pawCardId);

	void updatePawCard(Long pawCardId, UpdatePawCardRequest request);

	void deletePawCard(Long pawCardId);
}
