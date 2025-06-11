package com.example.rabbithell.domain.util.characterLogic;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.example.rabbithell.domain.job.entity.JobCategory;

public class ClassChange {

	private static final Random rand = new Random();

	// 스탯 목록
	private static final List<String> STATS = List.of("strength", "agility", "intelligence", "focus", "luck");

	// 직업별 주요 스탯 정의
	private static final Map<JobCategory, List<String>> JOB_MAJOR_STATS = Map.of(
		JobCategory.WARRIOR, List.of("strength"),
		JobCategory.WIZARD, List.of("intelligence"),
		JobCategory.ARCHER, List.of("agility"),
		JobCategory.THIEF, List.of("focus")
	);

	// 전직 구간: min ~ max 스탯
	private static final int[][] STAT_RANGES = {
		{100, 150}, // 0차
		{150, 200}, // 1차
		{200, 275}, // 2차
		{275, 375}, // 3차
		{375, 500}  // 4차
	};

	// 숙련도 기준치
	private static final int[][] SKILL_REQUIRED = {
		{0, 0},          // 0차
		{0, 10000},      // 1차
		{10000, 80000},  // 2차
		{80000, 99999},  // 3차
		{99999, 199999}  // 4차
	};

	// TODO 수정 필요
	public static int generateStat(int tier, int currentJobPoint) {

		// tier 범위 체크
		if (tier < 0 || tier >= STAT_RANGES.length) {
			throw new IllegalArgumentException("Invalid tier: " + tier);
		}

		int[] range = STAT_RANGES[tier];
		int min = range[0];
		int max = range[1] - 20;

		int[] skillReq = SKILL_REQUIRED[tier];
		int minSkill = skillReq[0];
		int maxSkill = skillReq[1];

		// 비율 계산: 0.0 ~ 1.0
		double ratio = (maxSkill - minSkill == 0) ? 1.0 :
			Math.max(0.0, Math.min(1.0, (double)(currentJobPoint - minSkill) / (maxSkill - minSkill)));

		// 편향값 계산: 비율로 중간값 이상으로 편향되도록
		double bias = min + (max - min) * ratio;

		// 편향된 정규 분포로 난수 생성 (편향값 중심 ±15% 범위에서)
		double stddev = (max - min) * 0.15;
		double value = rand.nextGaussian() * stddev + bias;

		// 클램핑
		int stat = (int)Math.round(Math.max(min, Math.min(value, max)));

		int offset = rand.nextInt(21);
		stat += offset;

		return stat;

	}

	public static int[] generateStatsByJobs(int tier, int currentJobPoint, List<JobCategory> jobs) {
		Set<JobCategory> majorStats = new HashSet<>(jobs);

		int strength = majorStats.contains(JobCategory.WARRIOR) ? generateStat(tier, currentJobPoint) : generateStat(0, currentJobPoint);
		int agility = majorStats.contains(JobCategory.ARCHER) ? generateStat(tier, currentJobPoint) : generateStat(0, currentJobPoint);
		int intelligence = majorStats.contains(JobCategory.WIZARD) ? generateStat(tier, currentJobPoint) : generateStat(0, currentJobPoint);
		int focus = majorStats.contains(JobCategory.THIEF) ? generateStat(tier, currentJobPoint) : generateStat(0, currentJobPoint);
		int luck = generateStatForLuck();

		return new int[] {strength, agility, intelligence, focus, luck};
	}

	public static int generateStatForLuck() {
		return rand.nextInt(100) + 1; // 1~100 사이
	}
}

