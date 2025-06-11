package com.example.rabbithell.domain.kingdom.dto.response;

import java.util.List;

import com.example.rabbithell.domain.kingdom.entity.Kingdom;
import com.example.rabbithell.domain.village.dto.response.VillageResponse;

public record KingdomResponse(
	Long id,
	String kingdomName,
	String kingdomDetail,
	List<VillageResponse> villags
) {
	public static KingdomResponse from(Kingdom kingdom) {
		return new KingdomResponse(
			kingdom.getId(),
			kingdom.getKingdomName(),
			kingdom.getKingdomDetail(),
			kingdom.getVillages().stream()
				.map(VillageResponse::from)
				.toList()
		);
	}
}
