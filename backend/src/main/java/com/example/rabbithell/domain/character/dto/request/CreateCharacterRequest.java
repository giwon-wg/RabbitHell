package com.example.rabbithell.domain.character.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCharacterRequest(

	@Schema(description = "캐릭터 명", example = "반말하지마")
	@NotBlank(message = "캐릭터 이름은 필수입니다.")
	@Size(max = 10, message = "캐릭터 이름은 10 글자 이하로 입력해주세요")
	String name

) {
}
