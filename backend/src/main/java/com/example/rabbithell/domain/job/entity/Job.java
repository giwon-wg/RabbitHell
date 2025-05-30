package com.example.rabbithell.domain.job.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Job {

    INCOMPETENT("무능한 토끼",JobTier.INCOMPETENT, 0, 0),

    WARRIOR_TIER1("전사 1차", JobTier.FIRST, 0, 2000),
    WARRIOR_TIER2("전사 2차", JobTier.SECOND, 10000, 5000),
    WARRIOR_TIER3("전사 3차", JobTier.THIRD, 80000, 10000),
    WARRIOR_TIER4("전사 4차", JobTier.FIRST, 99999, 50000),

    THIEF_TIER1("도적 1차", JobTier.FIRST, 0, 2000),
    THIEF_TIER2("도적 2차", JobTier.SECOND, 10000, 5000),
    THIEF_TIER3("도적 3차", JobTier.THIRD, 80000, 10000),
    THIEF_TIER4("도적 4차", JobTier.FIRST, 99999, 50000),

    MAGE_TIER1("마법사 1차", JobTier.FIRST, 0, 2000),
    MAGE_TIER2("마법사 2차", JobTier.SECOND, 10000, 5000),
    MAGE_TIER3("마법사 3차", JobTier.THIRD, 80000, 10000),
    MAGE_TIER4("마법사 4차", JobTier.FIRST, 99999, 50000),

    ARCHER_TIER1("궁수 1차", JobTier.FIRST, 0, 2000),
    ARCHER_TIER2("궁수 2차", JobTier.SECOND, 10000, 5000),
    ARCHER_TIER3("궁수 3차", JobTier.THIRD, 80000, 10000),
    ARCHER_TIER4("궁수 4차", JobTier.FIRST, 99999, 10000)
	;

    private final String name;
    private final JobTier tier;
    private final int requiredJobPoint;
    private final int requiredSubPoint;

}
