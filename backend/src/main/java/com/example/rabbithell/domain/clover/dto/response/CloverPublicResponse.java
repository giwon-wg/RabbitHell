package com.example.rabbithell.domain.clover.dto.response;

import com.example.rabbithell.domain.clover.entity.Clover;

public record CloverPublicResponse(
	Long id,
	String name,
	String kingdomName,
	String specieName
) {
	public static CloverPublicResponse from(Clover clover) {
		return new CloverPublicResponse(
			clover.getId(),
			clover.getName(),
			clover.getKingdom().getKingdomName(),
			clover.getSpecie().getSpeciesName()
		);
	}
}
