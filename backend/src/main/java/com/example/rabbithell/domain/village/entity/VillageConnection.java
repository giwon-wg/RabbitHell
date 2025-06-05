package com.example.rabbithell.domain.village.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "village_connection")
@NoArgsConstructor
public class VillageConnection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private Long fromVillage;

	@NotNull
	private Long toVillage;

	public VillageConnection(Long fromVillage, Long toVillage) {
		this.fromVillage = fromVillage;
		this.toVillage = toVillage;
	}

}
