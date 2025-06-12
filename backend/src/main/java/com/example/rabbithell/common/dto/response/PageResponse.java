package com.example.rabbithell.common.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponse<T>(
	List<T> content,
	int pageNumber,
	int pageSize,
	boolean hasNext
) {
	public static <T> PageResponse<T> of(List<T> content, Page<?> page) {
		return new PageResponse<>(
			content,
			page.getNumber(),
			page.getSize(),
			page.hasNext()
		);
	}
}
