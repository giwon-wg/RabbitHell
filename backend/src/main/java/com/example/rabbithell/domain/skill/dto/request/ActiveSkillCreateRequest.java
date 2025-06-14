package com.example.rabbithell.domain.skill.dto.request;

import com.example.rabbithell.domain.job.entity.Job;
import com.example.rabbithell.domain.skill.enums.SkillTarget;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActiveSkillCreateRequest(
	@Schema(description = "스킬명", example = "아도겐")
	@NotBlank(message = "스킬명 필수로 입력해주세요")
	String name,

	@Schema(description = "스킬 설명", example = "강려크한 공격 스킬입니다.")
	String description,

	@Schema(description = "요구 스킬 포인트", example = "5000")
	@NotNull(message = "스킬 포인트는 필수입니다.")
	int requiredSkillPoint,

	@Schema(description = "발동확률", example = "5")
	@NotNull(message = "발동 확률은 필수입니다")
	int probability,

	@Schema(description = "티어 (레벨)", example = "2")
	@NotNull(message = "티어는 필수입니다")
	int tier,

	@Schema(description = "MP 소모량", example = "10")
	@NotNull(message = "MP 소모량은 필수입니다")
	int mpCost,

	@Schema(description = "쿨타임 (초 단위)", example = "5")
	@NotNull(message = "쿨타임 필수입니다")
	int coolTime,

	@Schema(description = "스킬 데미지", example = "50")
	@NotNull(message = "데미지 필수입니다")
	int dmg,

	@Schema(description = "스킬 타겟(PERSONAL,CLOVER)", example = "CLOVER")
	@NotNull(message = "데미지 필수입니다")
	SkillTarget skillTarget,

	@Schema(description = "스킬이 속한 직업명", example = "WARRIOR_TIER1")
	@NotNull(message = "직업명 필수입니다")
	Job job
) {
}
