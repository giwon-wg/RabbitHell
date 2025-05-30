package com.example.rabbithell.domain.stigma.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.rabbithell.domain.stigma.entity.Stigma;

public record StigmaResponse(
	Long stigmaId,
	String name,
	BigDecimal ratio,
	String description,
	Boolean isDeleted,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt) {

	public static StigmaResponse fromEntity(Stigma stigma) {
		return new StigmaResponse(
			stigma.getId(),
			stigma.getName(),
			stigma.getRatio(),
			stigma.getDescription(),
			stigma.getIsDeleted(),
			stigma.getCreatedAt(),
			stigma.getModifiedAt());
	}
}
