package com.example.rabbithell.domain.monster.entity;

import com.example.rabbithell.domain.battleField.entity.BattleField;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "monster_encounter")
@NoArgsConstructor
@AllArgsConstructor
public class MonsterEncounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double spawnRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monster_id", nullable = false)
    private Monster monster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_field_id", nullable = false)
    private BattleField battleField;



}
