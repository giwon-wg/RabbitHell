package com.example.rabbithell.domain.village.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "village_connections")
@NoArgsConstructor
public class VillageConnection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Village fromVillage;

	@ManyToOne
	private Village toVillage;

	public VillageConnection(Village fromVillage, Village toVillage) {
		this.fromVillage = fromVillage;
		this.toVillage = toVillage;
	}

}
