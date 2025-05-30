package com.example.rabbithell.domain.stigmasocket.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.stigma.entity.Stigma;
import com.example.rabbithell.domain.stigma.repository.StigmaRepository;
import com.example.rabbithell.domain.stigmasocket.dto.request.CreateStigmaSocketRequest;
import com.example.rabbithell.domain.stigmasocket.dto.response.StigmaSocketResponse;
import com.example.rabbithell.domain.stigmasocket.entity.StigmaSocket;
import com.example.rabbithell.domain.stigmasocket.enums.StigmaSocketStatus;
import com.example.rabbithell.domain.stigmasocket.repository.StigmaSocketRepository;

@Service
@RequiredArgsConstructor
public class AdminStigmaSocketServiceImpl implements StigmaSocketService {

	private final StigmaSocketRepository stigmaSocketRepository;
	private final StigmaRepository stigmaRepository;
	private final CharacterRepository characterRepository;

	@Override
	public StigmaSocketResponse createStigmaSocket(CreateStigmaSocketRequest request) {
		Character character = characterRepository.findByIdOrElseThrow(request.characterId());
		Stigma stigma = stigmaRepository.findByIdOrElseThrow(request.stigmaId());
		StigmaSocket stigmaSocket = StigmaSocket.builder()
			.character(character)
			.stigma(stigma)
			.stigmaSocketStatus(StigmaSocketStatus.UNEQUIP)
			.build();
		stigmaSocketRepository.save(stigmaSocket);
		return StigmaSocketResponse.fromEntity(stigmaSocket, character, stigma);
	}
}
