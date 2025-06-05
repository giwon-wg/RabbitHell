package com.example.rabbithell.domain.village.service;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.clover.dto.response.CloverResponse;

public interface VillageService {

	void moveVillage(AuthUser authUser, Long villageId);

	CloverResponse  saveMoney(AuthUser authUser, int saveMoney);

	CloverResponse withdrawMoney(AuthUser authUser, int withdrawMoney);

	CloverResponse  cureCharacter(AuthUser authUser);

}
