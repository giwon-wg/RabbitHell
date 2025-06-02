package com.example.rabbithell.domain.inventory.entity;

import static com.example.rabbithell.domain.inventory.exception.code.InventoryExceptionCode.*;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.inventory.exception.InventoryException;
import com.example.rabbithell.domain.user.model.User;

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

@Entity
@Getter
@NoArgsConstructor
@Table(name = "inventory")
public class Inventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clover_id", nullable = false)
	private Clover clover;

	@Column(nullable = false)
	private Integer capacity; // 용량

	@Builder
	public Inventory(Clover clover, Integer capacity) {
		this.clover = clover;
		this.capacity = capacity;
	}

	public void expand(int amount) {
		if (amount <= 0) {
			throw new InventoryException(AMOUNT_TOO_SMALL);
		}
		this.capacity += amount;
	}

}
