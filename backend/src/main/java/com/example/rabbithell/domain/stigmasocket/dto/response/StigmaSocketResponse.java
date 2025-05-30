package com.example.rabbithell.domain.stigmasocket.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.stigma.entity.Stigma;
import com.example.rabbithell.domain.stigmasocket.entity.StigmaSocket;
import com.example.rabbithell.domain.stigmasocket.enums.StigmaSocketStatus;

public record StigmaSocketResponse(
	Long stigmaSocketId,
	Long characterId,
	Long stigmaId,
	String stigmaName,
	BigDecimal ratio,
	String description,
	StigmaSocketStatus status,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public static StigmaSocketResponse fromEntity(
		StigmaSocket stigmaSocket,
		Character character,
		Stigma stigma) {
		return new StigmaSocketResponse(
			stigmaSocket.getId(),
			character.getId(),
			stigma.getId(),
			stigma.getName(),
			stigma.getRatio(),
			stigma.getDescription(),
			stigmaSocket.getStigmaSocketStatus(),
			stigmaSocket.getCreatedAt(),
			stigmaSocket.getModifiedAt()
		);
	}
}
