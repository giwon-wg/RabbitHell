package com.example.rabbithell.domain.village.service;

import com.example.rabbithell.domain.auth.domain.AuthUser;

public interface VillageService {
	void moveVillage(AuthUser authUser, Long villageId);

	void saveMoney(AuthUser authUser, int saveMoney);

	void withdrawMoney(AuthUser authUser, int withdrawMoney);

	void cureCharacter(AuthUser authUser);

}
