package com.example.rabbithell.domain.clover.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.character.entity.GameCharacter;
import com.example.rabbithell.domain.deck.entity.PawCardEffect;
import com.example.rabbithell.domain.kingdom.entity.Kingdom;
import com.example.rabbithell.domain.specie.entity.Specie;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.village.entity.Village;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clover")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clover extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@OneToMany(mappedBy = "clover", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<GameCharacter> members = new ArrayList<>();

	private Integer stamina = 10000;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kingdom_id")
	private Kingdom kingdom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specie_id")
	private Specie specie;

	@Column(nullable = false)
	private long cash = 0;

	@Column(nullable = false)
	private long saving = 0;

	// 현재 마을 저장
	@Column(name = "current_village")
	private Long currentVillage;

	// 레어맵이 나오는데 나왔을때,
	@ElementCollection(fetch = FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	@CollectionTable(
		name = "character_unlocked_rare_maps",
		joinColumns = @JoinColumn(name = "character_id")
	)
	@Column(name = "rare_map_type")
	private Set<BattleFieldType> unlockedRareMaps = new HashSet<>();

	@OneToMany(mappedBy = "clover", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PawCardEffect> pawCardEffects = new ArrayList<>();

	@Builder
	public Clover(String name, User user, Kingdom kingdom, Specie specie, long cash, long saving, Long currentVillage,
		Set<BattleFieldType> unlockedRareMaps) {
		this.name = name;
		this.user = user;
		this.kingdom = kingdom;
		this.specie = specie;
		this.cash = cash;
		this.saving = saving;
		this.currentVillage = currentVillage;
		this.unlockedRareMaps = unlockedRareMaps;
	}

	// PawCard 추가
	public void addPawCardEffect(PawCardEffect pawCardEffect) {
		this.pawCardEffects.add(pawCardEffect);
		pawCardEffect.assignClover(this);
	}

	// 레어맵 컬럼에 추가
	public void unlockRareMap(BattleFieldType rareMap) {
		unlockedRareMaps.add(rareMap);
	}

	// 레어맵 휘발
	public void clearUnlockedRareMaps() {
		unlockedRareMaps.clear();
	}

	// 현재 마을 위치 업데이트?
	public void updateCurrentVillage(Village currentVillage) {
		this.currentVillage = currentVillage.getId();
	}

	public Clover(String name, User user) {
		this.name = name;
		this.user = user;
	}

	public void earnCash(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("획득 골드는 음수일 수 없습니다.");
		}
		this.cash += amount;
	}

	public void spendCash(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("사용 골드는 음수일 수 없습니다.");
		}
		if (this.cash < amount) {
			throw new IllegalStateException("보유 골드가 부족합니다.");
		}
		this.cash -= amount;
	}

	public void depositToSaving(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("입금 금액은 음수일 수 없습니다.");
		}
		if (this.cash < amount) {
			throw new IllegalStateException("골드가 부족합니다.");
		}
		this.cash -= amount;
		this.saving += amount;
	}

	public void withdrawFromSaving(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("출금 금액은 음수일 수 없습니다.");
		}
		if (this.saving < amount) {
			throw new IllegalStateException("은행 잔고가 부족합니다.");
		}
		this.saving -= amount;
		this.cash += amount;
	}

	//은행에서 돈 사용 메서드
	public void useFromSaving(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("출금 금액은 음수일 수 없습니다.");
		}
		if (this.saving < amount) {
			throw new IllegalStateException("은행 잔고가 부족합니다.");
		}
		this.saving -= amount;
		this.cash += amount;
	}

	public void addMember(GameCharacter character) {
		if (members.size() >= 4) {
			throw new IllegalStateException("원정대에는 4명까지만 추가할 수 있습니다.");
		}
		this.members.add(character);
	}

	// public void addPawCardEffect(PawCardEffect pawCardEffect) {
	// 	this.pawCardEffects.add(pawCardEffect);
	// 	pawCardEffect.assignClover(this);
	// }

	public boolean isFull() {
		return members.size() >= 4;
	}

	public boolean isMember(GameCharacter character) {
		return members.contains(character);
	}

}
