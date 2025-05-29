package com.example.rabbithell.domain.item.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item_effect")
public class ItemEffect {

    @EmbeddedId
    private ItemEffectId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId") // ItemEffectId.itemId 매핑
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("effectId") // ItemEffectId.effectId 매핑
    @JoinColumn(name = "effect_id")
    private Effect effect;

}
