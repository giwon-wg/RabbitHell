package com.example.rabbithell.domain.character.service;

import static com.example.rabbithell.domain.character.exception.code.CharacterExceptionCode.*;
import static com.example.rabbithell.domain.job.entity.Job.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
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
import com.example.rabbithell.domain.characterSkill.service.CharacterActiveSkillService;
import com.example.rabbithell.domain.characterSkill.service.CharacterPassiveSkillService;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.job.entity.JobCategory;
import com.example.rabbithell.domain.job.entity.JobTier;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.domain.util.characterLogic.ClassChange;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

	private final CharacterRepository characterRepository;
	private final UserRepository userRepository;
	private final CloverRepository cloverRepository;

	private static final Random random = new Random();
	private final CharacterActiveSkillService characterActiveSkillService;
	private final CharacterPassiveSkillService characterPassiveSkillService;

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
			.agility(100)
			.intelligence(100)
			.focus(100)
			.luck(100)
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
	public void validateChangeJob(Long authUserId, GameCharacter gameCharacter, Job changeJob) {

		if (gameCharacter.getLevel() < 50) {
			throw new CharacterException(LEVEL_IS_LOW);
		}

		Job currentJob = gameCharacter.getJob();

		// 지금 직업이 무능한 토끼일때
		if (currentJob == Job.INCOMPETENT && changeJob.getTier() == JobTier.FIRST) {
			if (gameCharacter.getIncompetentPoint() < 2000) {
				throw new CharacterException(CharacterExceptionCode.SKILL_POINT_IS_LOW);
			}
			return;
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

			if (mainPoint < changeJob.getRequiredJobPoint()) {
				throw new CharacterException(CharacterExceptionCode.MAIN_JOB_POINT_IS_LOW);
			}

			if (subPoint < changeJob.getRequiredSubPoint()) {
				throw new CharacterException(CharacterExceptionCode.SUB_JOB_POINT_IS_LOW);
			}

		}
	}

	@Transactional
	@Override
	public CharacterPersonalInfoResponse changeClass(Long authUserId, Long characterId, Job changeJob) {

		GameCharacter gameCharacter = characterRepository.findByIdOrElseThrow(characterId);

		validateChangeJob(authUserId, gameCharacter, changeJob);

		//전직 티어 기록 가져오기
		Map<JobCategory, Integer> jobHistory = gameCharacter.getJobHistory();

		// 2. 현재 전직할 직업군의 티어를 기존 기록과 비교해 갱신 (ex: 1차, 2차 등)
		int newTier = changeJob.getTier().getTier();
		int prevTier = jobHistory.getOrDefault(changeJob.getJobCategory(), 0);
		int updatedTier = Math.max(prevTier, newTier);

		// 3. 갱신된 티어 기록 저장
		gameCharacter.addJobTierHistory(changeJob.getJobCategory(), updatedTier);

		// 4. 전직할 직업군 포함, 경험한 모든 직업군과 티어를 모아서
		//    주요 스탯 생성에 반영할 리스트와 티어 맵 준비
		Map<JobCategory, Integer> jobTierForStats = new EnumMap<>(JobCategory.class);
		for (JobCategory category : JobCategory.values()) {
			int tier = jobHistory.getOrDefault(category, 0);
			jobTierForStats.put(category, tier);
		}

		// 새로 전직하는 직업군의 티어 반영
		jobTierForStats.put(changeJob.getJobCategory(), updatedTier);

		// 전직 경험이 있는 직업 리스트로
		List<JobCategory> experiencedJobs = jobTierForStats.entrySet().stream()
			.filter(e -> e.getValue() > 0)
			.map(Map.Entry::getKey)
			.toList();

		// 숙련도 가져오기
		int currentJobPoint = switch (changeJob.getJobCategory()) {
			case WARRIOR -> gameCharacter.getWarriorPoint();
			case THIEF -> gameCharacter.getThiefPoint();
			case WIZARD -> gameCharacter.getWizardPoint();
			case ARCHER -> gameCharacter.getArcherPoint();
			case INCOMPETENT -> gameCharacter.getIncompetentPoint();
		};

		// 가장 높은 티어와 경험한 직업군 리스트를 넣어 스탯 생성
		int maxTier = jobTierForStats.values().stream().max(Integer::compareTo).orElse(0);
		int[] newStats = ClassChange.generateStatsByJobs(maxTier, currentJobPoint, experiencedJobs);

		// 새 스탯 반영
		gameCharacter.updateStrength(newStats[0]);
		gameCharacter.updateAgility(newStats[1]);
		gameCharacter.updateIntelligence(newStats[2]);
		gameCharacter.updateFocus(newStats[3]);
		gameCharacter.updateLuck(newStats[4]);

		// 직업 업데이트
		gameCharacter.updateJob(changeJob);

		// 레벨초기화
		gameCharacter.updateLevel(1);

		// 경험치초기화
		gameCharacter.updateExp(0);

		characterRepository.save(gameCharacter);

		characterActiveSkillService.unequipActiveSkillsNotMatchingCurrentJob(gameCharacter);
		characterPassiveSkillService.unequipPassiveSkillsNotMatchingCurrentJob(gameCharacter);

		return CharacterPersonalInfoResponse.from(gameCharacter);

	}

}
