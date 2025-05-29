package com.example.rabbithell.domain.inventory.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class InventoryItemId implements Serializable {

    private Long inventoryId;
    private Long itemId;

}
