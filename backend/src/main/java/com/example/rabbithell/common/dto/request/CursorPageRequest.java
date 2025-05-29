package com.example.rabbithell.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CursorPageRequest {

    @Schema(description = "이전 요청에서 마지막으로 본 테이터의 ID", nullable = true)
    private Long cursor = 0L;

    @Schema(description = "조회할 데이터 수", nullable = true)
    @Min(1)
    @Max(30)
    private Integer size = 10;
}
