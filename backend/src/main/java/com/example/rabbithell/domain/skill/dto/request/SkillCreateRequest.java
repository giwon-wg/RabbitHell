package com.example.rabbithell.domain.skill.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SkillCreateRequest(
	@Schema(description = "스킬명", example = "아도겐")
	@NotBlank(message = "스킬명 필수로 입력해주세요")
	String name,

	@Schema(description = "스킬 설명", example = "강려크한 공격 스킬입니다.")
	String description,

	@Schema(description = "티어 (레벨)", example = "2")
	@NotNull(message = "티어는 필수입니다")
	Integer tier,

	@Schema(description = "MP 소모량", example = "10")
	@NotNull(message = "MP 소모량은 필수입니다")
	Integer mpCost,

	@Schema(description = "쿨타임 (초 단위)", example = "5")
	@NotNull(message = "쿨타임 필수입니다")
	Integer coolTime,

	@Schema(description = "스킬 데미지", example = "50")
	@NotNull(message = "데미지 필수입니다")
	Integer dmg,

	@Schema(description = "스킬이 속한 직업명", example = "전사 1차")
	@NotBlank(message = "직업명 필수입니다")
	String jobName
) {
}
