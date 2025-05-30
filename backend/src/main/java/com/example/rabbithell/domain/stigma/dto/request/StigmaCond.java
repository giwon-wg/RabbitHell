package com.example.rabbithell.domain.stigma.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record StigmaCond(
	@Schema(description = "삭제 여부 / ex) 삭제 = true", example = "false")
	Boolean isDeleted,

	@Schema(description = "스티그마 이름 검색 키워드", example = "흑염")
	String keyword
) {
}
