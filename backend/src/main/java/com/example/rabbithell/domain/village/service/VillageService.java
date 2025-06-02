package com.example.rabbithell.domain.village.service;

import static com.example.rabbithell.domain.village.exception.code.VillageExceptionCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.village.entity.Village;
import com.example.rabbithell.domain.village.exception.VillageException;
import com.example.rabbithell.domain.village.repository.VillageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VillageService {

	private final VillageRepository villageRepository;
	private final CharacterRepository characterRepository;
	private final CloverRepository cloverRepository;

	@Transactional
	public void moveVillage(AuthUser authUser, Long villageId) {

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		Village currentVillage = villageRepository.findByIdOrElseThrow(clover.getCurrentVillage());
		Village targetVillage = villageRepository.findByIdOrElseThrow(villageId);

		boolean isConnected = currentVillage.getConnections().stream()
			.anyMatch(conn -> conn.getToVillage().getId().equals(targetVillage.getId()));

		if (!isConnected) {
			throw new VillageException(VILLAGE_NOT_CONNECTED);
		}

		clover.updateCurrentVillage(targetVillage);
	}

	@Transactional
	public void saveMoney(AuthUser authUser, int saveMoney) {

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		clover.depositToSaving(Math.toIntExact(saveMoney));
	}

	@Transactional
	public void withdrawMoney(AuthUser authUser, int withdrawMoney) {

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		clover.withdrawFromSaving(withdrawMoney);

	}

	@Transactional
	public void cureCharacter(AuthUser authUser) {

		int cureCost = 0;

		// todo 클로버 주석 푼 후에 풀기
		// Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());
		//
		// List<Character> members = clover.getMembers();
		//
		// clover.useFromSaving(cureCost);
		//
		// for (Character member : members) {
		// 	member.refill();
		// }
	}
}
