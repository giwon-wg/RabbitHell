package com.example.rabbithell.domain.util.characterLogic;

import java.util.Random;

public class ClassChange {
	private static final Random rand = new Random();

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

	public static int generateStat(int tier, int currentSkill) {
		int[] range = STAT_RANGES[tier];
		int min = range[0];
		int max = range[1] - 20;

		int[] skillReq = SKILL_REQUIRED[tier];
		int minSkill = skillReq[0];
		int maxSkill = skillReq[1];

		// 비율 계산: 0.0 ~ 1.0
		double ratio = (maxSkill - minSkill == 0) ? 1.0 :
			Math.max(0.0, Math.min(1.0, (double)(currentSkill - minSkill) / (maxSkill - minSkill)));

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
}
