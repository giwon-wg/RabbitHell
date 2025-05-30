package com.example.rabbithell.domain.character.entity;

import java.util.HashSet;
import java.util.Set;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.kingdom.entity.Kingdom;
import com.example.rabbithell.domain.specie.entity.Specie;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.village.entity.Village;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "character")
public class Character extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kingdom_id",nullable = false)
    private Kingdom kingdom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id",nullable = false)
    private Specie specie;

    @Column(nullable = false,unique = true,length = 10)
    private String name;

    private String job;

    private int level;
    private int exp;
    private int stamina;

    private int maxHp;
    private int hp;
    private int maxMp;
    private int mp;

    private int strength;
    private int agility;
    private int intelligence;
    private int focus;
    private int luck;

    @Column(name = "warrior_point")
    private int warriorPoint;

    @Column(name = "thief_point")
    private int thiefPoint;

    @Column(name = "wizard_point")
    private int wizardPoint;

    @Column(name = "archer_point")
    private int archerPoint;

    private Long cash;
    private Long saving;

    @Column(name = "skill_point")
    private int skillPoint;

    @Column(name = "current_village")
    private Long currentVillage;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "character_unlocked_rare_maps",
        joinColumns = @JoinColumn(name = "character_id")
    )

    @Builder.Default
    @Column(name = "rare_map_type")
    private Set<BattleFieldType> unlockedRareMaps = new HashSet<>();

    // 레어맵 해금 메서드
    public void unlockRareMap(BattleFieldType rareMap) {
        unlockedRareMaps.add(rareMap);
    }

    public void clearUnlockedRareMaps() {
        unlockedRareMaps.clear();
    }

    public boolean hasUnlockedRareMap(BattleFieldType rareMap) {
        return unlockedRareMaps.contains(rareMap);
    }

    public void updateCurrentVillage(Village currentVillage) {
        this.currentVillage = currentVillage.getId();
    }

    public void saveToBank(Long saveMoney){
        this.cash -= saveMoney;
        this.saving += saveMoney;
    }

    public void withdrawFromBank(Long withdrawMoney){
        this.cash += withdrawMoney;
        this.saving -= withdrawMoney;
    }

    public void useMoneyFromSaving(Long useMoney){
        this.saving -= useMoney;
    }

    public void useMoneyFromCash(Long useMoney){
        this.cash -= useMoney;
    }

    public void refill(){
        this.hp = this.maxHp;
        this.mp = this.maxMp;
    }
}

