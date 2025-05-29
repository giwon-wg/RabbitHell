package com.example.rabbithell.domain.character.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCharacterRequest(

    @NotNull(message = "왕국은 필수 선택입니다.")
    Long kingdomId,

    @NotNull(message = "종족은 필수 선택입니다.")
    Long speciesId,

    @NotBlank(message = "캐릭터 이름은 필수입니다.")
    @Size(max = 10, message = "캐릭터 이름은 10 글자 이하로 입력해주세요")
    String name
) {
}
