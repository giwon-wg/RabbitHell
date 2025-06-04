package com.example.rabbithell.domain.clover.dto.response;

import com.example.rabbithell.domain.clover.entity.Clover;

public record CloverResponse(
	Long id,
	String name,
	Integer stamina,
	String kingdomName,
	String specieName,
	long cash,
	long saving
) {
	public static CloverResponse from(Clover clover) {
		return new CloverResponse(
			clover.getId(),
			clover.getName(),
			clover.getStamina(),
			clover.getKingdom().getKingdomName(),
			clover.getSpecie().getSpeciesName(),
			clover.getCash(),
			clover.getSaving()
		);
	}
}
