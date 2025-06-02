package com.example.rabbithell.domain.character.service;

import static com.example.rabbithell.domain.job.entity.Job.*;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.character.dto.request.CreateCharacterRequest;
import com.example.rabbithell.domain.character.dto.response.AllCharacterResponse;
import com.example.rabbithell.domain.character.dto.response.CharacterInfoResponse;
import com.example.rabbithell.domain.character.dto.response.CharacterPersonalInfoResponse;
import com.example.rabbithell.domain.character.dto.response.CharacterPublicInfoResponse;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.character.exception.CharacterException;
import com.example.rabbithell.domain.character.exception.code.CharacterExceptionCode;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.job.entity.JobCategory;
import com.example.rabbithell.domain.job.entity.JobTier;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

	private final CharacterRepository characterRepository;
	private final UserRepository userRepository;
	private final CloverRepository cloverRepository;

	private static final Random random = new Random();

	@Override
	public Long createCharacter(AuthUser authUser, CreateCharacterRequest request) {

		User user = userRepository.findByIdOrElseThrow(authUser.getUserId());
		Clover clover = cloverRepository.findByUserIdOrElseThrow(authUser.getUserId());

		GameCharacter gameCharacter = GameCharacter.builder()
			.user(user)
			.clover(clover)
			.name(request.name())
			.job(INCOMPETENT)
			.level(0)
			.exp(0)
			.maxHp(100)
			.hp(100)
			.maxMp(50)
			.mp(50)
			.strength(100)
			.minxStrength(200)
			.agility(100)
			.minAgility(200)
			.intelligence(100)
			.minIntelligence(200)
			.focus(100)
			.minFocus(200)
			.luck(8)
			.minLuck(200)
			.incompetentPoint(0)
			.warriorPoint(0)
			.thiefPoint(0)
			.wizardPoint(0)
			.archerPoint(0)
			.skillPoint(0)
			.build();

		clover.addMember(gameCharacter);
		characterRepository.save(gameCharacter);

		return gameCharacter.getClover().getId();
	}

	@Transactional(readOnly = true)
	@Override
	public CharacterInfoResponse characterInfo(Long characterId, AuthUser authUser) {

		GameCharacter gameCharacter = characterRepository.findByIdOrElseThrow(characterId);

		boolean isOwner = gameCharacter.getUser().getId().equals(authUser.getUserId());

		if (isOwner) {
			return CharacterPersonalInfoResponse.from(gameCharacter);
		} else {
			return CharacterPublicInfoResponse.from(gameCharacter);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<AllCharacterResponse> getAllCharacter(Long authUserId) {

		return characterRepository.findByUser_Id(authUserId).stream()
			.map(gameCharacter -> new AllCharacterResponse(
				gameCharacter.getClover().getId(),
				gameCharacter.getClover().getName(),
				gameCharacter.getId(),
				gameCharacter.getName(),
				gameCharacter.getJob().getName(),
				gameCharacter.getLevel(),
				gameCharacter.getMaxHp(),
				gameCharacter.getMaxMp(),
				gameCharacter.getStrength(),
				gameCharacter.getAgility(),
				gameCharacter.getIntelligence(),
				gameCharacter.getFocus(),
				gameCharacter.getLuck()))
			.toList();
	}

	@Override
	public boolean canChangeJob(Long authUserId, GameCharacter gameCharacter, Job changeJob) {

		if (gameCharacter.getLevel() < 50) {
			return false;
		}

		Job currentJob = gameCharacter.getJob();

		// 지금 직업이 무능한 토끼일때
		if (currentJob == Job.INCOMPETENT && changeJob.getTier() == JobTier.FIRST) {
			return gameCharacter.getIncompetentPoint() >= 2000;
		}

		// 현재 직업 스킬 포인트 필요. currentJob.get
		// 2차 전직 이상

		if (changeJob.getTier().isSameOrHigherThan(JobTier.FIRST)) {
			int mainPoint = switch (changeJob.getJobCategory()) {
				case WARRIOR -> gameCharacter.getWarriorPoint();
				case THIEF -> gameCharacter.getThiefPoint();
				case WIZARD -> gameCharacter.getWizardPoint();
				case ARCHER -> gameCharacter.getArcherPoint();
				default -> 0;
			};

			int subPoint = gameCharacter.getIncompetentPoint()
				+ (changeJob.getJobCategory() != JobCategory.WARRIOR ? gameCharacter.getWarriorPoint() : 0)
				+ (changeJob.getJobCategory() != JobCategory.THIEF ? gameCharacter.getThiefPoint() : 0)
				+ (changeJob.getJobCategory() != JobCategory.WIZARD ? gameCharacter.getWizardPoint() : 0)
				+ (changeJob.getJobCategory() != JobCategory.ARCHER ? gameCharacter.getArcherPoint() : 0);

			return mainPoint >= changeJob.getRequiredJobPoint()
				&& subPoint >= changeJob.getRequiredSubPoint();
		}

		return false;
	}

	@Transactional
	@Override
	public CharacterPersonalInfoResponse changeClass(Long authUserId, Long characterId, Job changeJob) {

		GameCharacter gameCharacter = characterRepository.findByIdOrElseThrow(characterId);

		if (!canChangeJob(authUserId, gameCharacter, changeJob)) {
			throw new CharacterException(CharacterExceptionCode.SKILL_POINT_IS_LOW);
		}

		gameCharacter.updateJob(changeJob);

		characterRepository.save(gameCharacter);

		return CharacterPersonalInfoResponse.from(gameCharacter);
	}

}
