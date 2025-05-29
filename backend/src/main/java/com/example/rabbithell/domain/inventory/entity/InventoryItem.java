package com.example.rabbithell.domain.inventory.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.example.rabbithell.domain.inventory.enums.Slot;
import com.example.rabbithell.domain.item.entity.Item;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "inventory_item")
public class InventoryItem {

    @EmbeddedId
    private InventoryItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("inventoryId") // InventoryItemId.inventoryId 매핑
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId") // InventoryItemId.itemId 매핑
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer durability;

    @Enumerated(EnumType.STRING)
    private Slot slot; // enum

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean is_deleted;

}
