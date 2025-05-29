package com.example.rabbithell.domain.battle.type;

import lombok.Getter;

@Getter
public enum BattleFieldType {
    PLAIN(false),
    MOUNTAIN(false),
    FOREST(false),
    DESERT(false),
    DARK_VALLEY(true),
    DRAGON_NEST(true),
    GOLDEN_FIELD(true);

    private final boolean isRare;

    BattleFieldType(boolean isRare) {
        this.isRare = isRare;
    }

}
