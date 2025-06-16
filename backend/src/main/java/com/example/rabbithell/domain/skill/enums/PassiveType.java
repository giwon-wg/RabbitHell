package com.example.rabbithell.domain.skill.enums;

public enum PassiveType {

	// 개인 능력 강화
	DEFENSE_UP,             // 방어력 증가
	ATTACK_UP,        // 기본 공격력 증가
	ATTACK_CHANCE,    // 공격 횟수 추가
	SKILL_DAMAGE_UP,        // 크리티컬 데미지 증가
	SKILL_ACTIVATION_UP,    // 스킬 발동률 증가

	// 클로버 지원형 효과
	CLOVER_CRITICAL_DAMAGE_UP,    // 팀원 크리티컬 데미지 증가
	CLOVER_CRITICAL_RATE_UP,      // 팀원 크리티컬 확률 증가
	CLOVER_MP_COST_DOWN       // 팀원 MP 소모량 감소
}
