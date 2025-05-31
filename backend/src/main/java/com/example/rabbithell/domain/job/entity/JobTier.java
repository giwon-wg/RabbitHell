package com.example.rabbithell.domain.job.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobTier {

	INCOMPETENT(0), FIRST(1), SECOND(2), THIRD(3), FOURTH(4);

	private final int tier;

	public boolean isSameOrHigherThan(JobTier other) {
		return this.tier >= other.tier;
	}
}
