package com.example.rabbithell.domain.stigma.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.rabbithell.domain.stigma.entity.Stigma;

public record StigamResponse(Long stigmaId, String name, BigDecimal ratio, String description, Boolean isDeleted,
							 LocalDateTime createdAt, LocalDateTime modifiedAt) {

	public static StigamResponse from(Stigma stigma) {
		return new StigamResponse(stigma.getId(), stigma.getName(), stigma.getRatio(), stigma.getDescription(),
			stigma.getIsDeleted(), stigma.getCreatedAt(), stigma.getModifiedAt());
	}
}
