package com.example.rabbithell.domain.character.dto.request;

import com.example.rabbithell.domain.job.entity.Job;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ClassChangeRequest(

	@Schema(description = "변경할 직업", example = "WARRIOR_TIER1")
	@NotNull
	Job job

) {
}
