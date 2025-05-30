package com.example.rabbithell.domain.expedition.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expeditions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expedition extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	// todo 캐릭터 쪽에
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "expedition_id")
	// private Expedition expedition;
	// 추가 후 주석 풀것
	// @OneToMany(mappedBy = "expedition", cascade = CascadeType.ALL, orphanRemoval = true)
	// private final List<com.example.rabbithell.domain.character.entity.Character> members = new ArrayList<>();

	@Column(nullable = false)
	private long gold = 0;

	@Column(nullable = false)
	private long bank = 0;

	//요청시 추가 사항
	@Column(name = "current_village")
	private Long currentVillage;



	//

	public Expedition(String name, User user) {
		this.name = name;
		this.user = user;
	}

	//로직
	public void earnGold(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("획득 골드는 음수일 수 없습니다.");
		}
		this.gold += amount;
	}

	public void spendGold(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("사용 골드는 음수일 수 없습니다.");
		}
		if (this.gold < amount) {
			throw new IllegalStateException("보유 골드가 부족합니다.");
		}
		this.gold -= amount;
	}

	public void depositToBank(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("입금 금액은 음수일 수 없습니다.");
		}
		if (this.gold < amount) {
			throw new IllegalStateException("골드가 부족합니다.");
		}
		this.gold -= amount;
		this.bank += amount;
	}

	public void withdrawFromBank(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("출금 금액은 음수일 수 없습니다.");
		}
		if (this.bank < amount) {
			throw new IllegalStateException("은행 잔고가 부족합니다.");
		}
		this.bank -= amount;
		this.gold += amount;
	}

	// public void addMember(Character character) {
	// 	if (members.size() >= 4) {
	// 		throw new IllegalStateException("원정대에는 4명까지만 추가할 수 있습니다.");
	// 	}
	// 	this.members.add(character);
	// 	// todo 캐릭터 생성 파트에 요청 필요
	// 	// character.setExpedition(this);
	//
	// 	// @ManyToOne(fetch = FetchType.LAZY)
	// 	// @JoinColumn(name = "expedition_id")
	// 	// private Expedition expedition;
	// 	//
	// 	// public void setExpedition(Expedition expedition) {
	// 	// 	this.expedition = expedition;
	// 	// }
	// }
	//
	// public boolean isFull() {
	// 	return members.size() >= 4;
	// }
	//
	// public boolean isMember(Character character) {
	// 	return members.contains(character);
	// }

}
