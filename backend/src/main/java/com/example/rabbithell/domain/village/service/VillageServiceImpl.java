package com.example.rabbithell.domain.village.service;

import static com.example.rabbithell.domain.village.exception.code.VillageExceptionCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.dto.response.CloverResponse;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.village.entity.Village;
import com.example.rabbithell.domain.village.entity.VillageConnection;
import com.example.rabbithell.domain.village.exception.VillageException;
import com.example.rabbithell.domain.village.repository.VillageConnectionRepository;
import com.example.rabbithell.domain.village.repository.VillageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VillageServiceImpl implements VillageService {

	private final VillageRepository villageRepository;
	private final CharacterRepository characterRepository;
	private final CloverRepository cloverRepository;
	private final VillageConnectionRepository villageConnectionRepository;

	@Override
	@Transactional
	public void moveVillage(AuthUser authUser, Long villageId) {

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		Village currentVillage = villageRepository.findByIdOrElseThrow(clover.getCurrentVillage());

		Village targetVillage = villageRepository.findByIdOrElseThrow(villageId);

		List<VillageConnection> connections = villageConnectionRepository.findByFromVillage(currentVillage.getId());
		boolean isConnected = connections.stream()
			.anyMatch(conn -> conn.getToVillage().equals(targetVillage.getId()));

		if (!isConnected) {
			throw new VillageException(VILLAGE_NOT_CONNECTED);
		}

		clover.updateCurrentVillage(targetVillage);
	}

	@Override
	@Transactional
	public CloverResponse saveMoney(AuthUser authUser, int saveMoney) {

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());
		clover.depositToSaving(saveMoney);
		return CloverResponse.from(clover);
	}

	@Transactional
	public CloverResponse  withdrawMoney(AuthUser authUser, int withdrawMoney) {

		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());
		clover.withdrawFromSaving(withdrawMoney);
		return CloverResponse.from(clover);

	}

	@Transactional
	public CloverResponse  cureCharacter(AuthUser authUser) {
		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

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

		return CloverResponse.from(clover);
	}
}
