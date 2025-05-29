package com.example.rabbithell.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CursorPageRequest(

    @Schema(description = "이전 요청에서 마지막으로 본 데이터의 ID", example = "123", nullable = true)
    Long cursor,

    @Schema(description = "조회할 데이터 수", example = "10", nullable = true)
    @Min(1)
    @Max(30)
    Integer size

) {
    public Long cursorId() {
        return cursor != null ? cursor : null;
    }

    public int sizeOrDefault() {
        return (size != null) ? size : 10;
    }
}
