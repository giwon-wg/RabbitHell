package com.example.rabbithell.domain.village.dto.response;

import com.example.rabbithell.domain.village.entity.Village;

public record VillageResponse(
	Long id,
	String name
) {
	public static VillageResponse from(Village village) {
		return new VillageResponse(
			village.getId(),
			village.getVillageName()
		);
	}
}
