package com.example.rabbithell.domain.inventory.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.example.rabbithell.domain.inventory.enums.Slot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int durability;

    @Enumerated(EnumType.STRING)
    private Slot slot; // 장착 부위 varchar(10) -> enum

    @Column(nullable = false)
    private Integer capacity; // 용량

}
